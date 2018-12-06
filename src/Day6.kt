import java.io.BufferedReader
import java.io.File
import kotlin.math.absoluteValue
import kotlin.test.assertEquals

/**
--- Day 6: Chronal Coordinates ---
The device on your wrist beeps several times, and once again you feel like you're falling.

"Situation critical," the device announces. "Destination indeterminate. Chronal interference detected. Please specify
new target coordinates."

The device then produces a list of coordinates (your puzzle input). Are they places it thinks are safe or dangerous? It
recommends you check manual page 729. The Elves did not give you a manual.

If they're dangerous, maybe you can minimize the danger by finding the coordinate that gives the largest distance from
the other points.

Using only the Manhattan distance, determine the area around each coordinate by counting the number of integer X,Y
locations that are closest to that coordinate (and aren't tied in distance to any other coordinate).

Your goal is to find the size of the largest area that isn't infinite. For example, consider the following list of
coordinates:

1, 1
1, 6
8, 3
3, 4
5, 5
8, 9

If we name these coordinates A through F, we can draw them on a grid, putting 0,0 at the top left:

..........
.A........
..........
........C.
...D......
.....E....
.B........
..........
..........
........F.

This view is partial - the actual grid extends infinitely in all directions. Using the Manhattan distance, each
location's closest coordinate can be determined, shown here in lowercase:

aaaaa.cccc
aAaaa.cccc
aaaddecccc
aadddeccCc
..dDdeeccc
bb.deEeecc
bBb.eeee..
bbb.eeefff
bbb.eeffff
bbb.ffffFf

Locations shown as . are equally far from two or more coordinates, and so they don't count as being closest to any.

In this example, the areas of coordinates A, B, C, and F are infinite - while not shown here, their areas extend forever
outside the visible grid. However, the areas of coordinates D and E are finite: D is closest to 9 locations, and E is
closest to 17 (both including the coordinate's location itself). Therefore, in this example, the size of the largest
area is 17.

What is the size of the largest area that isn't infinite?

--- Part Two ---
On the other hand, if the coordinates are safe, maybe the best you can do is try to find a region near as many
coordinates as possible.

For example, suppose you want the sum of the Manhattan distance to all of the coordinates to be less than 32. For each
location, add up the distances to all of the given coordinates; if the total of those distances is less than 32, that
location is within the desired region. Using the same coordinates as above, the resulting region looks like this:

..........
.A........
..........
...###..C.
..#D###...
..###E#...
.B.###....
..........
..........
........F.

In particular, consider the highlighted location 4,3 located at the top middle of the region. Its calculation is as
follows, where abs() is the absolute value function:

Distance to coordinate A: abs(4-1) + abs(3-1) =  5
Distance to coordinate B: abs(4-1) + abs(3-6) =  6
Distance to coordinate C: abs(4-8) + abs(3-3) =  4
Distance to coordinate D: abs(4-3) + abs(3-4) =  2
Distance to coordinate E: abs(4-5) + abs(3-5) =  3
Distance to coordinate F: abs(4-8) + abs(3-9) = 10
Total distance: 5 + 6 + 4 + 2 + 3 + 10 = 30

Because the total distance to all coordinates (30) is less than 32, the location is within the region.

This region, which also includes coordinates D and E, has a total size of 16.

Your actual region will need to be much larger than this example, though, instead including all locations with a total
distance of less than 10000.

What is the size of the region containing all locations which have a total distance to all given coordinates of less
than 10000?
 */
class Day6(): Day() {
	infix fun Any.`should equal`(expected: Any) = assertEquals(expected, this)
	inline fun <reified T> matrix(height: Int, width: Int, initialize: () -> T) =
			Array(height) {Array(width) {initialize()}}

	private fun subQuestion1(lineList: MutableList<String>): Int {
		var sheet = matrix(400,400) {""}
		val arrList = ArrayList<Pair<Int, Int>>()

		val regex = """(\d+), (\d+)""".toRegex()
		lineList.forEach {
			val coord = regex.find(it.trim())
			arrList.add(Pair(coord!!.destructured.component1().toInt(), coord.destructured.component2().toInt()))
		}

		// The 50 coordinates represent as 50 different characters
		val cList = mutableListOf<Char>()
		('a'..'y').forEach {e -> cList.add(e) }
		('A'..'Y').forEach {e -> cList.add(e) }
		for (ay in 0..399 ) {
			for (ax in 0..399) {
				var minDist = 800
				var ch = ""
				arrList.forEachIndexed { i, pair ->
					val dist = (ax - pair.first).absoluteValue + (ay - pair.second).absoluteValue
					if (dist < minDist) {
						minDist = dist
						ch = cList[i].toString()
					} else if (dist == minDist) {
						ch = "."
					}
				}
				sheet[ax][ay] = ch
			}
		}

		// Remove all characters that exists on the edges in the matrix
		var str = ""
		sheet.forEach { it.forEach { str += it } }
		for (y in 0..399) {
			if (y == 0 || y == 399) {
				for (x in 0..399) {
					str = str.replace(sheet[x][y], "")
				}
			} else {
				str = str.replace(sheet[0][y], "")
				str = str.replace(sheet[399][y], "")
			}
		}

		return str.groupingBy { it }.eachCount().values.max()!!.toInt()
	}

	private fun subQuestion2(lineList: MutableList<String>, distance: Int): Int {
		var sheet = matrix(400,400) {""}
		val arrList = ArrayList<Pair<Int, Int>>()

		val regex = """(\d+), (\d+)""".toRegex()
		lineList.forEach {
			val coord = regex.find(it.trim())
			arrList.add(Pair(coord!!.destructured.component1().toInt(), coord.destructured.component2().toInt()))
		}

		for (ay in 0..399 ) {
			for (ax in 0..399) {
				var dist = 0
				var ch = ""
				arrList.forEachIndexed { i, pair ->
					dist += (ax - pair.first).absoluteValue + (ay - pair.second).absoluteValue
				}
				if (dist < distance) {
					sheet[ax][ay] = "#"
				}
			}
		}
		var str = ""
		sheet.forEach { it.forEach { str += it } }
		return str.groupingBy { it }.eachCount().values.max()!!.toInt()
	}

	override fun show() {
		println("6 December:")

		val bufferedReader: BufferedReader = File("input/Day6.txt").bufferedReader()
		val lineList = mutableListOf<String>()
		bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }

		subQuestion1(mutableListOf("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9")) `should equal` (17)
		val s1 = System.currentTimeMillis()
		print("Answer part 1: " + subQuestion1(lineList))
		println("\t" + (System.currentTimeMillis() - s1) + " ms")

		subQuestion2(mutableListOf("1, 1", "1, 6", "8, 3", "3, 4", "5, 5", "8, 9"), 32) `should equal` (16)
		val s2 = System.currentTimeMillis()
		print("Answer part 2: " + subQuestion2(lineList, 10000))
		println("\t" + (System.currentTimeMillis() - s2) + " ms")
		println()

	}
}