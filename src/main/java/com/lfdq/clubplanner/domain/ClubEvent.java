package com.lfdq.clubplanner.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.lfdq.clubplanner.domain.enumeration.EventType;

/**
 * A ClubEvent.
 */
@Entity
@Table(name = "club_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "clubevent")
public class ClubEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "schedule")
    private String schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @ManyToOne
    private Site site;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "club_event_registrants",
               joinColumns = @JoinColumn(name="club_events_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="registrants_id", referencedColumnName="ID"))
    private Set<UserExtraInfo> registrants = new HashSet<>();

    @ManyToOne
    private Club club;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Set<UserExtraInfo> getRegistrants() {
        return registrants;
    }

    public void setRegistrants(Set<UserExtraInfo> userExtraInfos) {
        this.registrants = userExtraInfos;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClubEvent clubEvent = (ClubEvent) o;
        if(clubEvent.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, clubEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClubEvent{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", schedule='" + schedule + "'" +
            ", eventType='" + eventType + "'" +
            '}';
    }
}
