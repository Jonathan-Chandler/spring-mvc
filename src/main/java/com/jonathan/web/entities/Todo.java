package com.jonathan.web.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="todo")
public class Todo
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name="id")
  private int id;

  @Column(name="description")
  private String description;

  @Column(name="done")
  private boolean done;

  @Column(name="target_date")
  private java.sql.Date targetDate;
  //private java.util.Date targetDate;

  public Todo()
  {
  }
  
  public Todo(String description, boolean done, java.sql.Date targetDate)
  {
    this.description = description;
    this.done = done;
    this.targetDate = targetDate;
  }

  // get/set
  public int getId()
  {
    return this.id;
  }
  public void setId(int id)
  {
    this.id = id;
  }
  public String getDescription()
  {
    return this.description;
  }
  public void setDescription(String description)
  {
    this.description = description;
  }
  public boolean getDone()
  {
    return this.done;
  }
  public void setDone(boolean done)
  {
    this.done = done;
  }
  public java.sql.Date getTargetDate()
  {
    return this.targetDate;
  }
  public void setTargetDate(java.sql.Date targetDate)
  {
    this.targetDate = targetDate;
  }

  // convert to string
  @Override
  public String toString()
  {
    return "Todo [id=" + id + ", description=" + description + ", done=" + done + ", targetDate=" + targetDate + "]";
  }
}


