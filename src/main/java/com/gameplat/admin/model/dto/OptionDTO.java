package com.gameplat.admin.model.dto;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;

import java.io.Serializable;

@Data
public class OptionDTO<T> implements Serializable {
    private T value;
    private String text;

    public OptionDTO(T value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OptionDTO<?> optionDTO = (OptionDTO<?>) o;
        return Objects.equal(value, optionDTO.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("value", value).add("text", text).toString();
    }
}
