package kr.co.chunjae.ocrplatform.service.paper;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.chunjae.ocrplatform.dto.paper.PaperExcelSaveDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class PaperDownService {

    public void downloadExcel(HttpServletResponse response, List<PaperExcelSaveDTO> dataList) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data Sheet");
            Row headerRow = sheet.createRow(0);


            // 헤더 셀 생성
            String[] headers = {"번호", "출제년도", "출제타입", "월", "학년", "영역", "과목", "시험지명", "등록자", "등록일"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 데이터 리스트를 바탕으로 엑셀 파일 채우기
            int rowNum = 1; // 데이터를 채울 시작 행 번호

            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

            for (PaperExcelSaveDTO paperExcelSaveDTO : dataList) {
                Row row = sheet.createRow(rowNum++);

                // 각 컬럼에 해당하는 데이터 셀 생성 및 값 설정
                row.createCell(0).setCellValue(paperExcelSaveDTO.getId());           // 번호
                row.createCell(1).setCellValue(String.valueOf(paperExcelSaveDTO.getPastYear()));         // 출제년도
                row.createCell(2).setCellValue(paperExcelSaveDTO.getExamType());         // 출제타입
                row.createCell(3).setCellValue(paperExcelSaveDTO.getPastMonth());        // 월
                row.createCell(4).setCellValue(paperExcelSaveDTO.getGrade());        // 학년
                row.createCell(5).setCellValue(paperExcelSaveDTO.getArea());         // 영역
                row.createCell(6).setCellValue(paperExcelSaveDTO.getSubject());      // 과목
                row.createCell(7).setCellValue(paperExcelSaveDTO.getExamName());     // 시험지명
                row.createCell(8).setCellValue(paperExcelSaveDTO.getCreator());    // 등록자

                Cell dateCell = row.createCell(9);
                if (paperExcelSaveDTO.getCreateDate() != null) {
                    dateCell.setCellValue(paperExcelSaveDTO.getCreateDate());
                    dateCell.setCellStyle(dateStyle); // 셀에 날짜 형식 스타일 적용
                } else {
                    dateCell.setCellValue("");
                }
            }

            for (int i = 0; i <= 9; i++) {
                sheet.autoSizeColumn(i);
            }

            // 파일명 설정 및 다운로드

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH시mm분ss초");
            String formatedNow = now.format(formatter);


            String fileName = "dataList_" + formatedNow;
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx\"");

            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export data to Excel file", e);
        }
    }
}
