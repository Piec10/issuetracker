package com.piec10.issuetracker.entity;

import javax.persistence.*;

@Entity
@Table(name = "issue_tags")
public class IssueTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public IssueTag() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IssueTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
