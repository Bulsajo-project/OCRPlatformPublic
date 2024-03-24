package kr.co.chunjae.ocrplatform.mapper.ocr;

import kr.co.chunjae.ocrplatform.dto.ocr.OcrItemImageDTO;
import kr.co.chunjae.ocrplatform.dto.ocr.OcrPaperViewDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface OcrProgressMapper {

    // 아이템 ocr 업데이트
    public void updateItem(Map<String, Object> updateMap);
    public void insertItemImage(OcrItemImageDTO ocrItemImageDTO);
    public void deleteItemImage(Long id);
    public OcrPaperViewDTO selectPaper(Long paperId);
    public Map<String, String> selectPdfPath(Long paperId);
    public int selectCountItem(Long paperId);
    public Map<String, Object> selectOcrItemByOrder(Long paperId, int itemOrder);
    public List<Map<String, String>> selectItemImgList(Long itemId);
    public void updatePaperStatus(Long paperId);

    public void insertOperationLog(Long memberId, Long itemId);

    public Long selectPaperWorker(Long paperId);
}
