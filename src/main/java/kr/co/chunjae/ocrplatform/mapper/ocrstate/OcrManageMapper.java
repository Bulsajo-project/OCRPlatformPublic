package kr.co.chunjae.ocrplatform.mapper.ocrstate;

import kr.co.chunjae.ocrplatform.dto.common.TotalCounts;
import kr.co.chunjae.ocrplatform.dto.ocrstate.OcrExcelSaveDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.OcrListResponseDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.WorkerDTO;
import kr.co.chunjae.ocrplatform.dto.ocrstate.WorkerResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OcrManageMapper {
    List<OcrListResponseDTO> ocrListPage(@Param("params") Map<String, Object> params);

    TotalCounts totalCntOcr(@Param("data") Map<String, Object> data);

    List<OcrExcelSaveDTO> ocrExcelData(@Param("excelData") Map<String, Object> excelData);

    Integer updateWorkers(@Param("updateParams") Map<String, Object> updateParams);

    List<OcrListResponseDTO> ocrManageListPage(@Param("manageParams") Map<String, Object> manageParams);

    List<OcrExcelSaveDTO> ocrExcelMangeData(@Param("excelManageData") Map<String, Object> excelManageData);

    TotalCounts totalCntOcrManage(@Param("countData") Map<String, Object> countData);


    WorkerResponseDTO workerId(Integer id);

    Integer deleteWorker(WorkerDTO paperId);
}
