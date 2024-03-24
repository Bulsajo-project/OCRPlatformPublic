package kr.co.chunjae.ocrplatform.mapper.upload;

import kr.co.chunjae.ocrplatform.dto.upload.*;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UploadPaperMapper {
    int savePaper(UploadPaperDTO uploadPaperDTO);

    int savePDF(UploadPDFDTO uploadPDFDTO);

    int saveItem(UploadItemDTO uploadItemDTO);

    UploadSubjectDTO infoSubject(UploadPaperDTO uploadPaperDTO);
    UploadPaperDTO infoPDF(UploadPaperDTO uploadPaperDTO);

    UploadPDFDTO infoPDFFile(UploadPDFDTO uploadPDFDTO);

    UploadPaperDTO infoStatus(UploadPaperDTO uploadPaperDTO);

    int updatePaper(UploadPaperDTO uploadPaperDTO);
    int updatePDFFile(UploadPDFDTO uploadPDFDTO);

    int deletePaper(UploadRequestDTO requestDTO);

    int deletePDF (UploadRequestDTO requestDTO);

    int deleteItem(UploadRequestDTO requestDTO);
}
