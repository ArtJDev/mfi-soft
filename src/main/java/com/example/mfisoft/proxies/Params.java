package com.example.mfisoft.proxies;

public class Params {
    int _limit;
    int _start;

    public Params(int _limit, int _start) {
        this._limit = _limit;
        this._start = _start;
    }

    public int get_limit() {
        return _limit;
    }

    public void set_limit(int _limit) {
        this._limit = _limit;
    }

    public int get_start() {
        return _start;
    }

    public void set_start(int _start) {
        this._start = _start;
    }
}
