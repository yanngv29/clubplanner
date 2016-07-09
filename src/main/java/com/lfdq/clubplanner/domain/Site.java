package com.lfdq.clubplanner.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Site.
 */
@Entity
@Table(name = "site")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "site")
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "adress")
    private String adress;

    @Column(name = "map_link")
    private String mapLink;

    @Column(name = "is_gymnasium")
    private Boolean isGymnasium;

    @Column(name = "resident_club_name")
    private String residentClubName;

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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public Boolean isIsGymnasium() {
        return isGymnasium;
    }

    public void setIsGymnasium(Boolean isGymnasium) {
        this.isGymnasium = isGymnasium;
    }

    public String getResidentClubName() {
        return residentClubName;
    }

    public void setResidentClubName(String residentClubName) {
        this.residentClubName = residentClubName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Site site = (Site) o;
        if(site.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, site.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Site{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", adress='" + adress + "'" +
            ", mapLink='" + mapLink + "'" +
            ", isGymnasium='" + isGymnasium + "'" +
            ", residentClubName='" + residentClubName + "'" +
            '}';
    }
}
