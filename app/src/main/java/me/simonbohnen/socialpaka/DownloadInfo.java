package me.simonbohnen.socialpaka;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by nbeye on 09. Sep. 2017.
 */

public class DownloadInfo implements Serializable{
    private String inputName;
    private String name;
    private String bday;
    private String mail;
    private String phone;
    String[] skills;

    public DownloadInfo(String name, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.inputName = name;


            this.name = jsonObject.getString("fullName");
            this.bday = jsonObject.getString("bday");
            this.mail = jsonObject.getString("mail");
            this.phone = jsonObject.getString("phone");
            JSONArray jsonArray = jsonObject.getJSONArray("skills");
            skills = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                skills[i] = (String) jsonArray.get(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getInputName() {
        return inputName;
    }

    public String getName() {
        return name;
    }

    public String getBday() {
        return bday;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getSkills() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String skill : skills) {
            if (skill.equals(skills[skills.length - 1])) {
                stringBuilder.append(skill);
            } else {
                stringBuilder.append(skill + ",");
            }
        }
        return stringBuilder.toString();
    }
}
