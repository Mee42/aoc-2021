package dev.mee42

class MutBox<T>(var t: T) {
    fun set(newValue: T) {
        this.t = newValue
    }
    fun get(): T = t
    fun update(f: (T) -> T) {
        t = f(t)
    }
}