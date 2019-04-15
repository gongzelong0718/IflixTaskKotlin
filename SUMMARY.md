1. use threadpool to calculate the pi, now there will be an issue of too many threads.
2. When "Do not keep activies" in Develop option is clicked, there will be the following issue:
03-03 03:57:57.266 14553-14553/com.gongzelong.iflixtask W/Bundle: Key pi_elapse_time expected Long but value was a java.lang.Boolean.  The default value 0 was returned.
03-03 03:57:57.266 14553-14553/com.gongzelong.iflixtask W/Bundle: Attempt to cast generated internal exception:
It seems that pi_elapse_time is not saved correctly.