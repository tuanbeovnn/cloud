package e1800958.vamk.assign10;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Meeting implements Serializable {
    private String title;
    private String place;
    private List<String> participants;
    private String date;
    private String time;

    public Meeting(String title, String place, List<String> participants, String date, String time) {
        this.title = title;
        this.place = place;
        this.participants = participants;
        this.date = date;
        this.time = time;
    }

    public Meeting() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getParticipantText(){
        String res="";
        for(int i=0; i< participants.size()-1; i++){
            res+= participants.get(i)+ "\n";
        }
        res+= participants.get(participants.size()-1);
        return res;
    }
    public void setParticipantsText(String s){
        String[] participants= s.split("\n");
        this.participants= Arrays.asList(participants);
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "title='" + title + '\'' +
                ", place='" + place + '\'' +
                ", participants=" + participants.size() +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }


    public static int Search(ArrayList<Meeting> meetings, Meeting m){
        for (int i=0; i< meetings.size(); i++) {
            Meeting t=meetings.get(i);
            boolean check= m.getTitle().equals(t.getTitle()) && m.getPlace().equals(t.getPlace())
                    && m.getDate().equals(t.getDate()) && m.getTime().equals(t.getTime())
                    && m.getParticipantText().equals(t.getParticipantText());
            if (check) return i;
        }
        return -1;
    }

    public static ArrayList<Meeting> Update(ArrayList<Meeting> meetings, Meeting m, int index){
        if (index!=-1) {
            meetings.get(index).setTitle(m.getTitle());
            meetings.get(index).setPlace(m.getPlace());
            meetings.get(index).setParticipants(m.getParticipants());
            meetings.get(index).setDate(m.getDate());
            meetings.get(index).setTime(m.getTime());
        }

        return meetings;
    }

    public static  ArrayList<Meeting> SearchByDate(ArrayList<Meeting> meetings, String date){
        ArrayList<Meeting> res= new ArrayList<Meeting>();
        for (Meeting m: meetings) {
            if(m.getDate().equals(date)) res.add(m);
        }
        return  res;
    }

    public static  ArrayList<Meeting> SearchByTime(ArrayList<Meeting> meetings, String time){
        ArrayList<Meeting> res= new ArrayList<Meeting>();
        for (Meeting m: meetings) {
            if(m.getTime().equals(time)) res.add(m);
        }
        return  res;
    }

    public static  ArrayList<Meeting> SearchByParticipant(ArrayList<Meeting> meetings, String par){
        ArrayList<Meeting> res= new ArrayList<Meeting>();
        for (Meeting m: meetings) {
            if(m.getParticipants().contains(par)) res.add(m);
        }
        return  res;
    }
}