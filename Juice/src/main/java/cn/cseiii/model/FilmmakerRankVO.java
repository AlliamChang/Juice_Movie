package cn.cseiii.model;

import cn.cseiii.enums.FigureType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 53068 on 2017/6/11 0011.
 */
public class FilmmakerRankVO {

    private int id;
    private String avatar;
    private String name;
    private Set<FigureType> roleHasPlayed = new HashSet<>();
    private Double data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FigureType> getRoleHasPlayed() {
        return roleHasPlayed;
    }

    public void setRoleHasPlayed(Set<FigureType> roleHasPlayed) {
        this.roleHasPlayed = roleHasPlayed;
    }

    public Double getData() {
        return data;
    }

    public void setData(Double data) {
        this.data = data;
    }
}
