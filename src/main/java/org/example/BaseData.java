package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.util.Objects;

@Getter
@Entity
@Table(name = "base_data")
@Immutable
public class BaseData {
    @Id
    @JsonIgnore
    Integer idx;
    @Column
    @JsonProperty("식별자")
    String type;
    @Column
    @JsonProperty("순번")
    Integer num;
    @Column
    @JsonProperty("명칭")
    String name;
    @Column
    @JsonProperty("거리")
    Double distance;
    @Column
    @JsonProperty("시간")
    Integer time;
    @Column
    @JsonProperty("속도")
    Double speed;
    @Column
    @JsonProperty("기본")
    String basic;
    @Column(name = "calc")
    @JsonProperty("계산")
    Integer calc;

    @Transient
    Boolean isChange = false;

    public BaseData compareData(BaseData baseData) {
        BaseData compareBaseData = new BaseData();
        compareBaseData.type = type;

        if(Objects.nonNull(num) && !num.equals(baseData.num)) {
            compareBaseData.num = num;
            compareBaseData.isChange = isChange = baseData.isChange = true;
        }
        if(Objects.nonNull(name) && !name.equals(baseData.name)) {
            compareBaseData.name = name;
            compareBaseData.isChange = isChange = baseData.isChange = true;
        }
        if(Objects.nonNull(distance) && !distance.equals(baseData.distance)) {
            compareBaseData.distance = distance;
            compareBaseData.isChange = isChange = baseData.isChange = true;
        }
        if(Objects.nonNull(time) && !time.equals(baseData.time)) {
            compareBaseData.time = time;
            compareBaseData.isChange = isChange = baseData.isChange = true;
        }
        if(Objects.nonNull(speed) && !speed.equals(baseData.speed)) {
            compareBaseData.speed = speed;
            compareBaseData.isChange = isChange = baseData.isChange = true;
        }
        if(Objects.nonNull(basic) && !basic.equals(baseData.basic)) {
            compareBaseData.basic = basic;
            compareBaseData.isChange = isChange = baseData.isChange = true;
        }
        if(Objects.nonNull(calc) && !calc.equals(baseData.calc)) {
            compareBaseData.calc = calc;
            compareBaseData.isChange = isChange = baseData.isChange = true;
        }

        return compareBaseData;
    }
}
