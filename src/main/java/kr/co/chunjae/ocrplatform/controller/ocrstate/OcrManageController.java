package kr.co.chunjae.ocrplatform.controller.ocrstate;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.chunjae.ocrplatform.dto.common.SearchDTO;
import kr.co.chunjae.ocrplatform.dto.common.TotalCounts;
import kr.co.chunjae.ocrplatform.dto.ocrstate.OcrListResponseDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.OcrExcelSaveDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.WorkerDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.WorkerResponseDTO;
import kr.co.chunjae.ocrplatform.service.common.MessageService;
import kr.co.chunjae.ocrplatform.service.ocrstate.OcrDownService;
import kr.co.chunjae.ocrplatform.service.ocrstate.OcrManageService;
import kr.co.chunjae.ocrplatform.vo.apiResponse.codes.ResponseStatusCode;
import kr.co.chunjae.ocrplatform.vo.apiResponse.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OcrManageController {
    private final OcrManageService ocrManageService;
    private final OcrDownService ocrDownService;
    private final MessageService messageService;

    @PostMapping(value = "/ocrWorkList")
    public ResponseData<List<OcrListResponseDTO>> ocrList(
            HttpServletRequest request,
            @RequestBody SearchDTO ocrSearchDTO)
            throws Exception{

        try {
            Integer currentRole = (Integer) request.getSession().getAttribute("role");

            if (currentRole == null) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ROLE, null, messageService);
            } else if (currentRole != 1 && currentRole != 3 && currentRole != 4) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ACCESS, null, messageService);
            }


            List<OcrListResponseDTO> list = ocrManageService.viewList(ocrSearchDTO);
            if (list.isEmpty()) {
                return new ResponseData<>(ResponseStatusCode.ERROR_NODATA, list, messageService);
            }
            return new ResponseData<>(ResponseStatusCode.SUCCESS, list, messageService);
        } catch (Exception e){
            log.error("Error loading ocrWorkList: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping(value = "/ocrWorkTotal")
    public ResponseData<TotalCounts> totalCnt(
            HttpServletRequest request,
            @RequestBody SearchDTO ocrSearchDTO
    ) throws Exception{

        try{

            Integer currentRole = (Integer) request.getSession().getAttribute("role");
            if (currentRole == null) {
                currentRole = 99;
            }

            if (currentRole != 1 && currentRole != 3 && currentRole != 4) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ACCESS, new TotalCounts(), messageService);
            }

            TotalCounts totalCnt = ocrManageService.totalCountOcr(ocrSearchDTO);
            return new ResponseData<>(ResponseStatusCode.SUCCESS, totalCnt, messageService);
        } catch (Exception e){
            log.error("Error loading ocrWorkTotal: {}", e.getMessage(), e);
            throw e;
        }
    }

    // 엑셀 다운로드, form으로 데이터를 받아옴
    @PostMapping("/ocrWorkDownExcel")
    public void downs(HttpServletResponse response,
                      @ModelAttribute SearchDTO ocrSearchDTO) throws Exception {
        List<OcrExcelSaveDTO> dataList = ocrManageService.getDataList(ocrSearchDTO);
        ocrDownService.downExcel(response, dataList);
    }

    @PostMapping("/updateWorker")
    public ResponseData<WorkerResponseDTO> updateWorker(
            HttpServletRequest request,
            @RequestBody WorkerDTO paperId) throws Exception{
        try {

            WorkerResponseDTO workInfo = ocrManageService.getWorker(paperId);
            Long loginUserLong = (Long) request.getSession().getAttribute("id");
            Integer loginUser = loginUserLong != null ? loginUserLong.intValue() : null;
            Integer currentRole = (Integer) request.getSession().getAttribute("role");

            if (loginUser == null){
                return new ResponseData<>(ResponseStatusCode.ERROR_LOGIN, null, messageService);
            } else if (currentRole != 1 && currentRole != 3 && currentRole != 4) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ACCESS, null, messageService);
            } else if (currentRole == 1) { // 관리자일 경우 접근
                return new ResponseData<>(ResponseStatusCode.ADMIN, null, messageService);
            } else if (currentRole == 4) { // 검수자일 경우 접근
                return new ResponseData<>(ResponseStatusCode.CHECKS, null, messageService);
            }


            if (workInfo.getWorker() != null) {
                // worker가 이미 존재하는 경우
                if (workInfo.getWorker().equals(loginUser)) { // 로그인한 사용자와 worker의 ID가 동일한 경우
                    if (workInfo.getOcrComplete()==1){ // 작업이 완료된 경우
                        return new ResponseData<>(ResponseStatusCode.ERROR_COMPLETE, null, messageService);
                    }
                    return new ResponseData<>(ResponseStatusCode.SUCCESS_MOVE, null, messageService);
                }
                // worker가 null이 아닌 경우
                return new ResponseData<>(ResponseStatusCode.ERROR_EXISTS, null, messageService);
            }


            Integer update = ocrManageService.updateWorker(paperId, loginUser);
            if (update == 1){
                return new ResponseData<>(ResponseStatusCode.SUCCESS, null, messageService);
            } else {
                return new ResponseData<>(ResponseStatusCode.ERROR_FAIL, null, messageService);
            }
        } catch (Exception e){
            log.error("Error loading updateWorker: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping(value = "/ocrWorkManageList")
    public ResponseData<List<OcrListResponseDTO>> ocrMangeList(
            HttpServletRequest request,
            @RequestBody SearchDTO ocrSearchDTO)
            throws Exception{

        try {
            Integer currentRole = (Integer) request.getSession().getAttribute("role");
            if (currentRole == null) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ROLE, null, messageService);
            } else if (currentRole != 1) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ACCESS, null, messageService);
            }

            List<OcrListResponseDTO> list = ocrManageService.viewOcrMnageList(ocrSearchDTO);
            if (list.isEmpty()) {
                return new ResponseData<>(ResponseStatusCode.ERROR_NODATA, list, messageService);
            }
            return new ResponseData<>(ResponseStatusCode.SUCCESS, list, messageService);
        } catch (Exception e){
            log.error("Error loading ocrWorkManageList: {}", e.getMessage(), e);
            throw e;
        }
    }


    @PostMapping(value = "/ocrManageTotal")
    public ResponseData<TotalCounts> totalCnts(
            HttpServletRequest request,
            @RequestBody SearchDTO ocrSearchDTO
    ) throws Exception{

        try{
            Integer currentRole = (Integer) request.getSession().getAttribute("role");

            if (currentRole == null) {
                currentRole = 99;
            }

            if (currentRole != 1){
                return new ResponseData<>(ResponseStatusCode.ERROR_ACCESS, new TotalCounts(), messageService);
            }

            TotalCounts totalCnt = ocrManageService.totalCountOcrMange(ocrSearchDTO);
            return new ResponseData<>(ResponseStatusCode.SUCCESS, totalCnt, messageService);
        } catch (Exception e){
            log.error("Error loading ocrWorkManageList: {}", e.getMessage(), e);
            throw e;
        }
    }


    // 엑셀 다운로드, form으로 데이터를 받아옴
    @PostMapping("/ocrManageDownExcel")
    public void downloads(HttpServletResponse response,
                          @ModelAttribute SearchDTO ocrSearchDTO) throws Exception {

        List<OcrExcelSaveDTO> dataList = ocrManageService.getDataManageList(ocrSearchDTO);
        ocrDownService.downExcel(response, dataList);
    }

    @PostMapping("/deleteWorker")
    public ResponseData<WorkerResponseDTO> deleteWorker(
            HttpServletRequest request,
            @RequestBody WorkerDTO paperId) throws Exception{
        try {

            Long loginUserLong = (Long) request.getSession().getAttribute("id");
            Integer loginUser = loginUserLong != null ? loginUserLong.intValue() : null;
            Integer currentRole = (Integer) request.getSession().getAttribute("role");



            if (loginUser == null){
                return new ResponseData<>(ResponseStatusCode.ERROR_LOGIN, null, messageService);
            } else if (currentRole != 1) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ACCESS, null, messageService);
            }

            WorkerResponseDTO workInfo = ocrManageService.getWorker(paperId);

            if (workInfo.getWorker() == null) {
                return new ResponseData<>(ResponseStatusCode.NOWORKER, null, messageService);
            }


            Integer delete = ocrManageService.deleteWorker(paperId);

            if (delete == 1){
                return new ResponseData<>(ResponseStatusCode.SUCCESS, null, messageService);
            } else {
                return new ResponseData<>(ResponseStatusCode.ERROR_FAIL, null, messageService);
            }
        } catch (Exception e){
            log.error("Error loading deleteWorker: {}", e.getMessage(), e);
            throw e;
        }
    }

}