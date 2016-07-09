package com.lfdq.clubplanner.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.lfdq.clubplanner.domain.enumeration.UserType;

/**
 * A UserExtraInfo.
 */
@Entity
@Table(name = "user_extra_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userextrainfo")
public class UserExtraInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany(mappedBy = "registrants")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ClubEvent> events = new HashSet<>();

    @ManyToOne
    private Team team;

    @ManyToOne
    private Team trainedTeam;

    @ManyToOne
    private Team coachedTeam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ClubEvent> getEvents() {
        return events;
    }

    public void setEvents(Set<ClubEvent> clubEvents) {
        this.events = clubEvents;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTrainedTeam() {
        return trainedTeam;
    }

    public void setTrainedTeam(Team team) {
        this.trainedTeam = team;
    }

    public Team getCoachedTeam() {
        return coachedTeam;
    }

    public void setCoachedTeam(Team team) {
        this.coachedTeam = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserExtraInfo userExtraInfo = (UserExtraInfo) o;
        if(userExtraInfo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userExtraInfo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserExtraInfo{" +
            "id=" + id +
            ", nickname='" + nickname + "'" +
            ", userType='" + userType + "'" +
            '}';
    }
}
