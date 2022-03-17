package br.com.sw2you.realmeet.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import br.com.sw2you.realmeet.domain.model.Employee;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "allocation")
public class Allocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Embedded
    private Employee employee;

    @Column(name = "subject")
    private String subject;

    @Column(name = "start_at")
    private OffsetDateTime startAt;

    @Column(name = "end_at")
    private OffsetDateTime endAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public Allocation() {
    }

    private Allocation(Builder builder) {
        this.id = builder.id;
        this.room = builder.room;
        this.employee = builder.employee;
        this.subject = builder.subject;
        this.startAt = builder.startAt;
        this.endAt = builder.endAt;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getSubject() {
        return subject;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Allocation that = (Allocation) o;
        return Objects.equals(id, that.id) && Objects.equals(room, that.room) && Objects.equals(employee, that.employee) && Objects.equals(subject, that.subject) && Objects.equals(startAt, that.startAt) && Objects.equals(endAt, that.endAt) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, room, employee, subject, startAt, endAt, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("room", room)
                .append("employee", employee)
                .append("subject", subject)
                .append("startAt", startAt)
                .append("endAt", endAt)
                .append("createdAt", createdAt)
                .append("updatedAt", updatedAt)
                .toString();
    }

    public static final class Builder {
        private Long id;
        private Room room;
        private Employee employee;
        private String subject;
        private OffsetDateTime startAt;
        private OffsetDateTime endAt;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder room(Room room) {
            this.room = room;
            return this;
        }

        public Builder employee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder startAt(OffsetDateTime startAt) {
            this.startAt = startAt;
            return this;
        }

        public Builder endAt(OffsetDateTime endAt) {
            this.endAt = endAt;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Allocation build() {
            return new Allocation(this);
        }

    }
}
