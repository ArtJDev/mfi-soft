package com.example.mfisoft.proxies;

public class Params {

    Integer _limit;

    Integer _start;

    public Params(Integer _limit, Integer _start) {
        this._limit = _limit;
        this._start = _start;
    }

    public Integer get_limit() {
        return _limit;
    }

    public void set_limit(Integer _limit) {
        this._limit = _limit;
    }

    public Integer get_start() {
        return _start;
    }

    public void set_start(Integer _start) {
        this._start = _start;
    }
}
