package kr.co.chunjae.ocrplatform.controller.paper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.chunjae.ocrplatform.dto.paper.PaperExcelSaveDTO;
import kr.co.chunjae.ocrplatform.dto.common.SearchDTO;
import kr.co.chunjae.ocrplatform.dto.common.TotalCounts;
import kr.co.chunjae.ocrplatform.service.common.MessageService;
import kr.co.chunjae.ocrplatform.service.paper.PaperDownService;
import kr.co.chunjae.ocrplatform.vo.apiResponse.codes.ResponseStatusCode;
import kr.co.chunjae.ocrplatform.vo.apiResponse.response.ResponseData;
import kr.co.chunjae.ocrplatform.dto.paper.PaperListResponseDTO;
import kr.co.chunjae.ocrplatform.service.paper.PaperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PaperController {
    private final PaperService paperService;
    private final PaperDownService paperDownService;
    private final MessageService messageService;
    @PostMapping(value = "/paperList")
    public ResponseData<List<PaperListResponseDTO>> paperList(
            HttpServletRequest request,
            @RequestBody SearchDTO paperSearchDTO
    ) throws Exception{
        try {
            Integer currentRole = (Integer) request.getSession().getAttribute("role");

            if (currentRole == null) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ROLE, null, messageService);
            } else if (currentRole != 1 && currentRole != 2) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ACCESS, null, messageService);
            }

            List<PaperListResponseDTO> list = paperService.viewPaperList(paperSearchDTO);
            if (list.isEmpty()) {
                return new ResponseData<>(ResponseStatusCode.ERROR_NODATA, list, messageService);
            }
            return new ResponseData<>(ResponseStatusCode.SUCCESS, list, messageService);
        } catch (Exception e) {
            log.error("Error loading paperList: {}", e.getMessage(), e);
            throw e;
        }

    }

    @PostMapping(value = "/totalCount")
    public ResponseData<TotalCounts> totalCount(
            HttpServletRequest request,
            @RequestBody SearchDTO paperSearchDTO
    ) throws  Exception{

        try {
            Integer currentRole = (Integer) request.getSession().getAttribute("role");
            if (currentRole == null) {
                currentRole = 99;
            }

            if (currentRole != 1 && currentRole != 2) {
                return new ResponseData<>(ResponseStatusCode.ERROR_ACCESS, new TotalCounts(), messageService);
            }

            TotalCounts totalCnt = paperService.totalCount(paperSearchDTO);
            return new ResponseData<>(ResponseStatusCode.SUCCESS, totalCnt, messageService);
        } catch (Exception e){
            log.error("Error loading totalCount: {}", e.getMessage(), e);
            throw e;
        }

    }


    // 엑셀 다운로드, form으로 데이터를 받아옴
    @PostMapping("/downloadExcel")
    public void downloadExcel(HttpServletResponse response,
                              @ModelAttribute SearchDTO paperSearchDTO) throws Exception {
        // 검색 조건을 사용하여 데이터 조회
        List<PaperExcelSaveDTO> dataList = paperService.getDataList(paperSearchDTO);
        paperDownService.downloadExcel(response, dataList);
    }
}
