package kr.co.chunjae.ocrplatform.mapper.paper;

import kr.co.chunjae.ocrplatform.dto.paper.PaperExcelSaveDTO;
import kr.co.chunjae.ocrplatform.dto.paper.PaperListResponseDTO;
import kr.co.chunjae.ocrplatform.dto.common.TotalCounts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PaperMapper {
    // Map<String, Object>를 사용하여 파라미터를 전달하는 방식으로 변경
    List<PaperListResponseDTO> paperListPage(@Param("params") Map<String, Object> params);

    TotalCounts totalCnt(@Param("data") Map<String, Object> data);

    List<PaperExcelSaveDTO> excelData(@Param("excelData") Map<String, Object> excelData);
}
