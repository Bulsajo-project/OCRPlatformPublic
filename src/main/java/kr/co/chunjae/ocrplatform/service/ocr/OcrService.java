package kr.co.chunjae.ocrplatform.service.ocr;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.chunjae.ocrplatform.dto.ocr.*;
import kr.co.chunjae.ocrplatform.mapper.ocr.OcrProgressMapper;
import kr.co.chunjae.ocrplatform.utils.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrService {

  private final OcrProgressMapper ocrProgressMapper;

  @Value("${jwt.clovaOCR.apiURL}")
  private String clovaApiURL;
  @Value("${jwt.clovaOCR.secretKey}")
  private String clovaOCRSecretKey;

  @Value("${jwt.mathpix.url}")
  private String mathpixURL;
  @Value("${jwt.mathpix.app_id}")
  private String mathpixAppId;
  @Value("${jwt.mathpix.app_key}")
  private String mathpixAppKey;

  @Value("${custom.path.upload}")
  private String uploadPath;

  public void checkMember(Long paperId, HttpServletRequest request) {
    String role = request.getSession().getAttribute("role").toString();
    if(role.equals("1") || role.equals("4")){ //총괄관리자, 문제검수자는 통과

    } else if (role.equals("3")) {//문제 운영자는 해당 시험지 담당자인 경우 통과
        Long memberId = (Long) request.getSession().getAttribute("id");
        Long paperWorker = ocrProgressMapper.selectPaperWorker(paperId);
        if(!memberId.equals(paperWorker)){
          throw new ForbiddenException("403 Forbidden");
        }

    } else{ //이외 모든 항목 403
      throw new ForbiddenException("403 Forbidden");
    }
  }
  public OcrPaperViewDTO ocrPaperView(Long paperId) {
    OcrPaperViewDTO ocrPaperViewDTO = ocrProgressMapper.selectPaper(paperId);
    return ocrPaperViewDTO;
  }

  public Map<String, Object> ocrItemView(Map<String, String> params) {
    Long paperId = Long.valueOf(params.getOrDefault("paper_id","1"));
    int itemOrder = Integer.parseInt(params.getOrDefault("setOrd", "1"));

    Map<String, Object> itemResultMap = ocrProgressMapper.selectOcrItemByOrder(paperId, itemOrder);
    List<Map<String, String>> imageList = ocrProgressMapper.selectItemImgList((Long) itemResultMap.get("id"));

    Map<String, Object> response = new HashMap<>();

    response.put("item_Id",itemResultMap.get("id"));
    response.put("ocr_result",itemResultMap.get("ocr_result"));
    response.put("image_list", imageList);

    return response;
  }

  public String getPdfPath(Long paperId) {
    Map<String, String> pdfPathMap = ocrProgressMapper.selectPdfPath(paperId);
    String pdfFullPath = pdfPathMap.get("path") + pdfPathMap.get("upload_file");
    return pdfFullPath;
  }

  public int countItem(Long paperId) {
    int count = ocrProgressMapper.selectCountItem(paperId);

    return count;
  }

  public Map<String, Object> editOcrText(EditOcrTextRequsetDTO editOcrTextRequsetDTO, HttpServletRequest request) {
    Long paperId = editOcrTextRequsetDTO.getPaperId();
    int itemOrder = editOcrTextRequsetDTO.getSetOrd();

    Map<String, Object> itemResultMap = ocrProgressMapper.selectOcrItemByOrder(paperId, itemOrder);
    Long id = (Long) itemResultMap.get("id");

    String ocrResult = editOcrTextRequsetDTO.getOcrText();
    Long updater = (Long) request.getSession().getAttribute("id");
    updateItem(ocrResult, id, updater);

    Map<String, Object> response = new HashMap<>();

    response.put("result","OK");
    response.put("ocr_result", ocrResult);


    return response;
  }

  public ProgressResponseDTO progress(ProgressRequestDTO progressRequestDTO, HttpServletRequest request)
          throws IOException, MethodArgumentNotValidException {

    //너무기니까 살짝 줄이기, 참조값만 이동해서 성능영향없음
    List<String> imageTypeList = progressRequestDTO.getImgTypeList();
    List<String> imgSrcList = progressRequestDTO.getImgSrcList();

    List<File> ocrTargetImgList = new ArrayList<>(); //ocr 대상 리스트
    List<File> itemImgList = new ArrayList<>(); // 전체 이미지 리스트

    Map<String, Object> itemResultMap =
            ocrProgressMapper.selectOcrItemByOrder(progressRequestDTO.getPaperId(), progressRequestDTO.getSetOrd());
    Long itemId = (Long) itemResultMap.get("id");

    if (imageTypeList.size() == 1){ // imageTypeList가 1개이면,  get() 메소드가 스트링을 List로 인식해서 예외처리
      if (Objects.nonNull(imgSrcList)) {
        File file = saveFile(imgSrcList.toString(), itemId, request);
        if (imageTypeList.get(0).equals("T")) {
          ocrTargetImgList.add(file); //ocr 대상으로 추가
        }
        itemImgList.add(file); // 전체 이미지 리스트에 추가
      }
    }else{
      for (int i = 0; i < imgSrcList.size(); i++) {
        if (Objects.nonNull(imgSrcList.get(i))) {
          File file = saveFile(imgSrcList.get(i), itemId, request);
          if (imageTypeList.get(i).equals("T")) {
            ocrTargetImgList.add(file); //ocr 대상으로 추가
          }
          itemImgList.add(file); // 전체 이미지 리스트에 추가
        }
      }
    }

    String ocrTxt = "";
    String subjCode = progressRequestDTO.getSubjCode();
    if(subjCode.equals("MA")){ //수학인 경우
      ocrTxt = getOcrMath(ocrTargetImgList); //수식용 ocr 적용
    } else if (subjCode.equals("SO")) { //수학 외 과목인 경우
      ocrTxt = getOcrLiteral(ocrTargetImgList); //텍스트용 ocr 적용
    }else{

    }

    //결과값 선언과 초기화
    ProgressResponseDTO response = new ProgressResponseDTO();

    // OCR Result가 너무 긴경우 DB 저장없이 다시 리턴
    if(ocrTxt.length() >= 21844){
      response.setResult("ERR_TOO_LONG");
      return response;
    }
    /**

     DB 저장 시작

     **/
    //이미지 파일 메타정보 DB 저장
    Long memberId = (Long) request.getSession().getAttribute("id"); //업데이트 하는 사람
    saveItemImageInfo(itemImgList, imageTypeList, itemId, memberId);

    //아이템 메타정보 DB 업데이트
    updateItem(ocrTxt, itemId, memberId);

    //ocr 변환 중 상태가 아닌경우, 변환중 상태로 변경
    checkPaperStatus(progressRequestDTO.getPaperId());
    //작업 로그 기록
    writeOperationLog(memberId, itemId);
    /**

     DB 저장 끝

     **/

    //결과값 세팅
    response.setItem_id(itemId);
    response.setOcrTxt(ocrTxt);
    response.setCluster_text(ocrTxt);
    response.setElastic_text(ocrTxt);
    response.setSubj_code(subjCode);
    response.setResult("OK");


    return response;
  }

  private void checkPaperStatus(Long paperId){
    ocrProgressMapper.updatePaperStatus(paperId);
  }

  private void writeOperationLog(Long memberId, Long itemId){
    ocrProgressMapper.insertOperationLog(memberId, itemId);
  }

  //tb_item에 ocr 변환 내역 업데이트
  private void updateItem(String ocrTxt, Long itemId, Long memberId){
    Map<String,Object> updateMap = new HashMap();
    updateMap.put("ocrResult", ocrTxt);
    updateMap.put("id", itemId);
    updateMap.put("updater", memberId);
    ocrProgressMapper.updateItem(updateMap);
  }

  private void saveItemImageInfo(List<File> itemImgList, List<String> imageTypeList, Long itemId, Long memberId){
    //insert 이전, itemId에 해당하는 기존 이미지가 있을시 삭제
    ocrProgressMapper.deleteItemImage(itemId);

    for (int i = 0; i < itemImgList.size(); i++) { //이미지 개수만큼 DB에 메타데이터 저장로직 실행
      OcrItemImageDTO ocrItemImageDTO = new OcrItemImageDTO();

      ocrItemImageDTO.setItemId(itemId);
      // 프론트 사용시 적합한 경로형태로 변경
      String AbsolutePath = itemImgList.get(i).getParent();
      String convertedPath = AbsolutePath.replace("\\", "/").replaceFirst(".*?/upload", "");
      ocrItemImageDTO.setPath(convertedPath+"/");

      ocrItemImageDTO.setUploadFile(itemImgList.get(i).getName());
      ocrItemImageDTO.setCreator(memberId);
      ocrItemImageDTO.setUpdater(memberId);
      ocrItemImageDTO.setImageType(imageTypeList.get(i).toString());

      ocrProgressMapper.insertItemImage(ocrItemImageDTO);
    }


  }

  private File saveFile(String imgSrc, Long itemId, HttpServletRequest request) throws IOException {

    //해당 문자열이 Base64인지 확인
    File file = null;
    if (imgSrc.startsWith("[data:image/jpeg;base64") || imgSrc.startsWith("data:image/jpeg;base64")) { //base64 라면
      //itemId로 폴더 만들어서 저장
      String path = uploadPath +"img/"+ itemId;
      File dirFileObject = new File(path);
      dirFileObject.mkdir();
      path = path + "/";
      String fileName = UUID.randomUUID()+".jpg";
      String completeFilePath = path + fileName;
      file = base64ToFile(imgSrc, completeFilePath);
    }else{ //base64 가 아니면, 이미 저장된 파일을 객체로 넘김
      // Remove the square brackets and leading slash
      String cleanedString = imgSrc.replaceAll("[\\[\\]]", "").substring(1);
      // Replace forward slashes with backslashes
      String pathString = cleanedString.replace("/", "\\");
      file = new File(uploadPath +"\\" + pathString);
    }


    return file;
  }

  private static File base64ToFile(String imgSrc, String filePath) throws IOException {
    String base64Image = imgSrc.split(",")[1];

    File file = new File(filePath);

    byte[] imageBytes = Base64.decodeBase64(base64Image);
    BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imageBytes));
    ImageIO.write(bufImg, "jpg", file);
    return file;
  }

  // 수식용 ocr
  private String getOcrMath(List<File> ocrTargetImgList) throws IOException {
    StringBuilder clusterText = new StringBuilder();

    for (File file : ocrTargetImgList) {
      if (file.exists()) {
        String response = sendApiRequest(file, mathpixURL, mathpixAppKey, mathpixAppId);

        // Parse JSON response
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);

        //복수 이미지 요청 결과를 붙이기
        clusterText.append(responseMap.get("text"));
        clusterText.append(" ");
      }
    }
    return clusterText.toString();
  }

  // 텍스트용 ocr
  private String getOcrLiteral(List<File> ocrTargetImgList) throws IOException {
    StringBuilder elasticText = new StringBuilder();
    for (File file : ocrTargetImgList) {
      if (file.exists()) {
        String response = sendApiRequest(file, clovaApiURL, clovaOCRSecretKey,"");

        // Parse JSON response
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
        List<Map<String, Object>> imageInfo = (List<Map<String, Object>>) responseMap.get("images");


        List<Map<String, Object>> fields = (List<Map<String, Object>>) imageInfo.get(0).get("fields");

        // Extract inferText values
        for (Map<String, Object> field : fields) {
          String inferText = (String) field.get("inferText");
          elasticText.append(inferText);
          elasticText.append(" ");
        }
      }
    }

    return elasticText.toString();
  }

  //s3 환경 미제공으로, 이미지 url이 아닌 파일로 요청하기 위해 multipart/form-data로 바디를 구성
  private String sendApiRequest(File file, String apiURL, String key, String id) throws IOException{
    URL url = new URL(apiURL);
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setUseCaches(false);
    con.setDoInput(true);
    con.setDoOutput(true);
    con.setReadTimeout(30000);
    con.setRequestMethod("POST");
    String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
    con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

    String jsonName = "";
    JSONObject json = new JSONObject();

    if(id.equals("")){ //clova 요청시
      //api 스펙에 따른 Authorization 헤더 작성
      con.setRequestProperty("X-OCR-SECRET", key);

      //api 스펙에 따른 jsonMessage 작성
      json.put("version", "V1");
      json.put("requestId", UUID.randomUUID().toString());
      json.put("timestamp", System.currentTimeMillis());

      JSONObject image = new JSONObject();
      image.put("format", "jpg");
      image.put("name", "temp");
      JSONArray images = new JSONArray();
      images.put(image);
      json.put("images", images);
      jsonName = "message";

    } else { // Mathpix 요청시
      //api 스펙에 따른 Authorization 헤더 작성
      con.setRequestProperty("app_id", id);
      con.setRequestProperty("app_key", key);

      //api 스펙에 따른 jsonMessage 작성
      json.put("rm_spaces", true);
      json.put("emath_inline_delimiters", "[\"$\", \"$\"]");
      jsonName = "options_json";
    }

    String postParams = json.toString();

    con.connect();
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());

    writeMultiPart(wr, jsonName, postParams, file, boundary);
    wr.close();

    int responseCode = con.getResponseCode();
    BufferedReader br;
    if (responseCode == 200) {
      br = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = br.readLine()) != null) {
      response.append(inputLine);
    }
    br.close();


    return response.toString();
  }


  private static void writeMultiPart(OutputStream out, String jsonName, String jsonMessage, File file, String boundary) throws
          IOException {
    StringBuilder sb = new StringBuilder();
    sb.append("--").append(boundary).append("\r\n");
    sb.append("Content-Disposition:form-data; name=\""+jsonName+"\"\r\n\r\n");
    sb.append(jsonMessage);
    sb.append("\r\n");

    out.write(sb.toString().getBytes(StandardCharsets.UTF_8));
    out.flush();

    if (file != null && file.isFile()) {
      out.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
      StringBuilder fileString = new StringBuilder();
      fileString
              .append("Content-Disposition:form-data; name=\"file\"; filename=");
      fileString.append("\"" + file.getName() + "\"\r\n");
      fileString.append("Content-Type: application/octet-stream\r\n\r\n");
      out.write(fileString.toString().getBytes(StandardCharsets.UTF_8));
      out.flush();

      try (FileInputStream fis = new FileInputStream(file)) {
        byte[] buffer = new byte[8192];
        int count;
        while ((count = fis.read(buffer)) != -1) {
          out.write(buffer, 0, count);
        }
        out.write("\r\n".getBytes());
      }

      out.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
    }
    out.flush();
  }


}