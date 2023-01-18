package com.piec10.issuetracker.entity;

import javax.persistence.*;

@Entity
@Table(name = "issue_statuses")
public class IssueStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public IssueStatus() {
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
        return "IssueStatus{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
