package kr.co.chunjae.ocrplatform.mapper.common;

import kr.co.chunjae.ocrplatform.dto.common.AreaDTO;
import kr.co.chunjae.ocrplatform.dto.common.SubjectDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SelectMapper {

    List<SubjectDTO> selecteData(AreaDTO id);

    List<AreaDTO> loadAreas();
}
