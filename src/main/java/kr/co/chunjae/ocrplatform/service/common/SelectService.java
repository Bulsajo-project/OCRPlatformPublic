package kr.co.chunjae.ocrplatform.service.common;

import kr.co.chunjae.ocrplatform.dto.common.AreaDTO;
import kr.co.chunjae.ocrplatform.dto.common.SubjectDTO;
import kr.co.chunjae.ocrplatform.mapper.common.SelectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SelectService {
    private final SelectMapper commonMapper;


    public List<SubjectDTO> selectSubject(AreaDTO id) {
        return commonMapper.selecteData(id);
    }

    public List<AreaDTO> loadArea() {
        return commonMapper.loadAreas();
    }
}
