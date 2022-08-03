package se.hgbrg.adventofcode2021

fun <I, O> memoize(function: (arg: I) -> O): (arg: I) -> O {
    val cache = hashMapOf<I, O>()
    return { arg ->
        var result = cache[arg]
        if (result != null) {
            result
        } else {
            result = function(arg)
            cache[arg] = result
            result
        }
    }
}
