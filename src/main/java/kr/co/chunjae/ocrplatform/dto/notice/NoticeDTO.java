package kr.co.chunjae.ocrplatform.dto.notice;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;

@Data
public class NoticeDTO {
  private Long id;
  private Long creator; // fk
  private String title;
  private String content;
  private Timestamp createDate;
  private Timestamp updateDate;
  private Long updater;
  private Timestamp deleteDate;
  private Long deleter;


  public NoticeDTO() {
  }

  public NoticeDTO(Long id, String title, String content) {
    this.id = id;
    this.title = title;
    this.content = content;
  }
}
