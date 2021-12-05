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
fun MutBox<Int>.incr() {
    update { it + 1 }
}

data class Coords2D(val x: Int, val y: Int)


class Array2D<T> private constructor(val list: List<List<MutBox<T>>>) {

    fun get(x: Int, y: Int): T   = list[x][y].get()
    fun get(coords: Coords2D): T = getMutBox(coords).get()
    fun getMutBox(coords: Coords2D): MutBox<T> = list[coords.x][coords.y]

    fun set(x: Int, y: Int, value: T) = list[x][y].set(value)
    fun set(coords: Coords2D, value: T) = set(coords.x, coords.y, value)

    fun toList(): List<T> = list.flatten().map { it.get() }
    val coordsMatrix by lazy { init(list.size, list[0].size) { it } }
    val coordsList by lazy { coordsMatrix.toList() }

    fun forEach(f: (T, Coords2D) -> Unit) {
        for(coords in coordsList) {
            f(get(coords), coords)
        }
    }
    fun forEachUpdate(f: (T, Coords2D) -> T) {
        for(coords in coordsList) {
            val elem = getMutBox(coords)
            elem.update { f(it, coords) }
        }
    }
    fun <R> map(f: (T, Coords2D) -> R): Array2D<R> {
        return Array2D(list.mapIndexed { x, xList -> xList.mapIndexed { y, value -> MutBox(f(value.get(), Coords2D(x, y))) } })
    }

    companion object {
        fun <T> initSquare(size: Int, default: (Coords2D) -> T): Array2D<T> {
            return init(size, size, default)
        }
        fun <T> init(xSize: Int, ySize: Int, default: (Coords2D) -> T): Array2D<T> {
            return Array2D(List(xSize) { x -> List(ySize) { y -> MutBox(default(Coords2D(x, y))) } })
        }
    }
}