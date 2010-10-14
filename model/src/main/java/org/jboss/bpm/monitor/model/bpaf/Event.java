package org.jboss.bpm.monitor.model.bpaf;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Every state change in the BPAF state model can be represented as an XML message following the BPAF Event Format.
 * 
 *
 * <p>This format has the following basic components:
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventDetails">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="CurrentState" use="required" type="{http://www.wfmc.org/2009/BPAF2.0}State" />
 *                 &lt;attribute name="PreviousState" type="{http://www.wfmc.org/2009/BPAF2.0}State" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="DataElement" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="EventID" use="required" type="{http://www.wfmc.org/2009/BPAF2.0}ID" />
 *       &lt;attribute name="ServerID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ProcessDefinitionID" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ProcessInstanceID" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ProcessName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ActivityDefinitionID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ActivityInstanceID" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *       &lt;attribute name="ActivityName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Timestamp" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "eventDetails",
    "dataElement"
})
@XmlRootElement(name = "Event")
@Entity
@Table(name="BPAF_EVENT")
public class Event {

  @XmlElement(name = "EventDetails", required = true)
  protected Event.EventDetails eventDetails;

  @XmlElement(name = "DataElement")
  protected List<Tuple> dataElement;

  /**
   * A globally unique identifier for the individual event
   */
  @XmlAttribute(name = "EventID", required = true)
  protected long eventID;

  /**
   * [optional]: A globally unique identifier for the originating server of the event
   */
  @XmlAttribute(name = "ServerID")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NMTOKEN")
  protected String serverID;

  /**
   * The identifier of the process definition
   * from which the current process instance has been derived.
   */
  @XmlAttribute(name = "ProcessDefinitionID", required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NMTOKEN")
  protected String processDefinitionID;

  /**
   * The identifier of the process instance
   * that serves as the context of the event.
   */
  @XmlAttribute(name = "ProcessInstanceID", required = true)
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NMTOKEN")
  @Column(name = "PROCESS_INSTANCE_ID")
  protected String processInstanceID;

  /**
   * [optional]: The name of the process definition
   * from which the current process instance has been derived.
   */
  @XmlAttribute(name = "ProcessName")
  @Column(name = "PROCESS_NAME")
  protected String processName;

  /**
   * [optional]: The identifier of the activity definition
   * from which the current activity instance has been derived.
   */
  @XmlAttribute(name = "ActivityDefinitionID")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NMTOKEN")
  @Column(name = "ACTIVITY_DEFINITION_ID")
  protected String activityDefinitionID;

  /**
   * [optional]: The identifier of the activity instance
   * that serves as the context of the event.
   */
  @XmlAttribute(name = "ActivityInstanceID")
  @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
  @XmlSchemaType(name = "NMTOKEN")
  @Column(name = "ACTIVITY_IINSTANCE_ID")
  protected String activityInstanceID;

  /**
   * [optional]: The name of the activity definition
   * from which the current activity instance has been derived.
   */
  @XmlAttribute(name = "ActivityName")
  @Column(name = "ACTIVITY_NAME")
  protected String activityName;

  /**
   * The time of the event occurrence
   */
  @XmlAttribute(name = "Timestamp", required = true)
  @XmlSchemaType(name = "long")
  @Column(name = "TIMESTAMP")
  protected long timestamp;

  public Event()
  {
  }

  public Event(boolean implicitTimestamp)
  {
    if(implicitTimestamp)
      this.timestamp = System.currentTimeMillis();
  }

  /**
   * Gets the value of the eventDetails property.
   *
   * @return
   *     possible object is
   *     {@link Event.EventDetails }
   *
   */
  public Event.EventDetails getEventDetails() {
    if(null==eventDetails)
      eventDetails = new EventDetails();
    return eventDetails;
  }

  /**
   * Sets the value of the eventDetails property.
   *
   * @param value
   *     allowed object is
   *     {@link Event.EventDetails }
   *
   */
  public void setEventDetails(Event.EventDetails value) {
    this.eventDetails = value;
  }

  /**
   * A name-value-pair that can be used to store additional process data
   * that can later be used to correlate or aggregate events.
   * 
   * @return
   */
  @OneToMany(mappedBy="event", fetch=FetchType.EAGER, cascade = CascadeType.ALL)
  public List<Tuple> getDataElement() {
    if (dataElement == null) {
      dataElement = new ArrayList<Tuple>();
    }
    return this.dataElement;
  }

  public void addData(Tuple tuple)
  {
    tuple.setEvent(this);
    getDataElement().add(tuple);
  }

  public void setDataElement(List<Tuple> data) {
    this.dataElement = data;
  }

  /**
   * Gets the value of the eventID property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  @Column(name = "EID")
  public long getEventID() {
    return eventID;
  }

  /**
   * Sets the value of the eventID property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setEventID(long value) {
    this.eventID = value;
  }

  /**
   * Gets the value of the serverID property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  @Basic(optional = true)
  @Column(name = "SERVER_ID")
  public String getServerID() {
    return serverID;
  }

  /**
   * Sets the value of the serverID property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setServerID(String value) {
    this.serverID = value;
  }

  /**
   * Gets the value of the processDefinitionID property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  @Basic
  @Column(name = "PROCESS_DEFINITION_ID")
  public String getProcessDefinitionID() {
    return processDefinitionID;
  }

  /**
   * Sets the value of the processDefinitionID property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setProcessDefinitionID(String value) {
    this.processDefinitionID = value;
  }

  /**
   * Gets the value of the processInstanceID property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  @Basic
  @Column(name = "PROCESS_INSTANCE_ID")
  public String getProcessInstanceID() {
    return processInstanceID;
  }

  /**
   * Sets the value of the processInstanceID property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setProcessInstanceID(String value) {
    this.processInstanceID = value;
  }

  /**
   * Gets the value of the processName property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  @Basic(optional = true)
  @Column(name = "PROCESS_NAME")
  public String getProcessName() {
    return processName;
  }

  /**
   * Sets the value of the processName property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setProcessName(String value) {
    this.processName = value;
  }

  /**
   * Gets the value of the activityDefinitionID property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  @Basic(optional = true)
  @Column(name = "ACTIVITY_DEFINITION_ID")
  public String getActivityDefinitionID() {
    return activityDefinitionID;
  }

  /**
   * Sets the value of the activityDefinitionID property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setActivityDefinitionID(String value) {
    this.activityDefinitionID = value;
  }

  /**
   * Gets the value of the activityInstanceID property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  @Basic(optional = true)
  @Column(name = "ACTIVITY_INSTANCE_ID")
  public String getActivityInstanceID() {
    return activityInstanceID;
  }

  /**
   * Sets the value of the activityInstanceID property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setActivityInstanceID(String value) {
    this.activityInstanceID = value;
  }

  /**
   * Gets the value of the activityName property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  @Basic(optional = true)
   @Column(name = "ACTIVITY_NAME")
  public String getActivityName() {
    return activityName;
  }

  /**
   * Sets the value of the activityName property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setActivityName(String value) {
    this.activityName = value;
  }

  /**
   * Gets the value of the timestamp property.
   *
   * @return
   *     possible object is
   *     {@link XMLGregorianCalendar }
   *
   */
  @Column(name = "TIMESTAMP")
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the timestamp property.
   *
   * @param value
   *     allowed object is
   *     {@link XMLGregorianCalendar }
   *
   */
  public void setTimestamp(long value) {
    this.timestamp = value;
  }


  /**
   * <p>Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;complexContent>
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
   *       &lt;attribute name="CurrentState" use="required" type="{http://www.wfmc.org/2009/BPAF2.0}State" />
   *       &lt;attribute name="PreviousState" type="{http://www.wfmc.org/2009/BPAF2.0}State" />
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "")
  @Embeddable
  public static class EventDetails {

    /**
     * An identifier of the current state
     * of the object that changed state, derived from the BPAF state model
     */
    @XmlAttribute(name = "CurrentState", required = true)
    protected State currentState;

    /**
     * [optional]: An identifier of
     * the previous state of the object that changed state,
     * derived from the BPAF state model
     */
    @XmlAttribute(name = "PreviousState")
    protected State previousState;

    @Basic()
    @Enumerated(EnumType.STRING)
    @Column(name = "CURRENT_STATE")
    public State getCurrentState() {
      return this.currentState;
    }

    public void setCurrentState(State currentState)
    {
      this.currentState = currentState;
    }

    @Basic(optional = true)
    @Enumerated(EnumType.STRING)
    @Column(name = "PREVIOUS_STATE")
    public State getPreviousState() {
      return this.previousState;
    }

    public void setPreviousState(State previousState)
    {
      this.previousState = previousState;
    }

    @Override
    public String toString()
    {
      return "EventDetails{" +
          "currentState=" + currentState +
          '}';
    }
  }

  @Override
  public String toString()
  {
    return "Event{" +
        "timestamp=" + timestamp +
        ", processDefinitionID='" + processDefinitionID + '\'' +
        ", processInstanceID='" + processInstanceID + '\'' +
        ", activityDefinitionID='" + activityDefinitionID + '\'' +    
        ", eventDetails=" + eventDetails +            
        '}';
  }
}
