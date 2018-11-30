fun main(args: Array<String>) {
	var days: MutableCollection<Day> = mutableListOf(Day1(),Day2())
	days.first().welcome()
	if (args.isEmpty()) {
		for (day in days.iterator()){
			day.show()
		}
	} else {
		days.elementAt(args[0].toInt()).show()
	}
}