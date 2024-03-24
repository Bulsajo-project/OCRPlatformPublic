package kr.co.chunjae.ocrplatform.controller.common;

import kr.co.chunjae.ocrplatform.dto.common.AreaDTO;
import kr.co.chunjae.ocrplatform.dto.common.SubjectDTO;
import kr.co.chunjae.ocrplatform.service.common.MessageService;
import kr.co.chunjae.ocrplatform.service.common.SelectService;
import kr.co.chunjae.ocrplatform.vo.apiResponse.codes.ResponseStatusCode;
import kr.co.chunjae.ocrplatform.vo.apiResponse.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SelectController {
    private final SelectService commonService;
    private final MessageService messageService;


    @PostMapping(value = "/loadArea")
    public ResponseData<List<AreaDTO>> AraData()throws Exception{
        try {
            List<AreaDTO> AreaList = commonService.loadArea();
            return new ResponseData<>(ResponseStatusCode.SUCCESS, AreaList, messageService);
        } catch (Exception e){
            log.error("Error loading areas: {}", e.getMessage(), e);
            throw e;
        }
    }


    @PostMapping(value = "/subject")
    public ResponseData<List<SubjectDTO>> subjectData(@RequestBody AreaDTO id)throws Exception{
        try {
            List<SubjectDTO> subjectList = commonService.selectSubject(id);
            return new ResponseData<>(ResponseStatusCode.SUCCESS, subjectList, messageService);
        } catch (Exception e){
            log.error("Error loading subject: {}", e.getMessage(), e);
            throw e;
        }
    }
}
