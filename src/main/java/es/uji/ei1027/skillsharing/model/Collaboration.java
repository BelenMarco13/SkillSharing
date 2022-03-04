package es.uji.ei1027.skillsharing.model;



import es.uji.ei1027.skillsharing.Score;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class Collaboration {
    private int idRequest;
    private int idOffer;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
    private Score score;
    private String comment;

    public Collaboration() {
    }

    public int getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(int id) {
        this.idRequest = id;
    }

    public int getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(int id) {
        this.idOffer = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getScore() {
        return score.getValor();
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public String toString() {
        return "Collaboration{" +
                "idRequest=" + idRequest +
                ", idOffer='" + idOffer + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", score=" + score + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

}












