package com.finby.model.removebg_api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorRoot {
    private List<Error> errors;
}
