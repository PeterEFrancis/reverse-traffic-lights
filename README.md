# reverse-traffic-lights

In a usual game of traffic lights, opponents alternate clicking squares on an `m x n` board and by doing so, progress squares forward (empty, green, yellow, red). The board begins empty and once a square is red, it cannot be changed. The players race to make a row, column, or diagonal of three consecutive squares of the same color.

Here we search for the most possible clicks that can be made on a board without ever having state with three co-linear uni-color squares.

##### Some results:

```
Starting full tree search on 1x3 board ...
Finished: (8 clicks) The most possible clicked board is 
  012
0 YRR


Starting full tree search on 1x4 board ...
Finished: (11 clicks) The most possible clicked board is 
  0123
0 RYRR


Starting full tree search on 1x5 board ...
Finished: (14 clicks) The most possible clicked board is 
  01234
0 RRYRR


Starting full tree search on 1x6 board ...
Finished: (16 clicks) The most possible clicked board is 
  012345
0 YRRYRR


Starting full tree search on 1x7 board ...
Finished: (19 clicks) The most possible clicked board is 
  0123456
0 RYRRYRR


Starting full tree search on 1x8 board ...
Finished: (22 clicks) The most possible clicked board is 
  01234567
0 RRYRRYRR


Starting full tree search on 2x3 board ...
Finished: (16 clicks) The most possible clicked board is 
  012
0 YRR
1 YRR


Starting full tree search on 2x4 board ...
Finished: (22 clicks) The most possible clicked board is 
  0123
0 RYRR
1 RYRR


Starting full tree search on 2x5 board ...
Finished: (28 clicks) The most possible clicked board is 
  01234
0 RRYRR
1 RRYRR


Starting full tree search on 2x6 board ...
Finished: (32 clicks) The most possible clicked board is 
  012345
0 YRRYRR
1 YRRYRR


Starting full tree search on 2x7 board ...
Finished: (38 clicks) The most possible clicked board is 
  0123456
0 RYRRYRR
1 RYRRYRR


Starting full tree search on 3x3 board ...
Finished: (23 clicks) The most possible clicked board is 
  012
0 GRR
1 RYR
2 RRY


Starting full tree search on 3x4 board ...
Finished: (31 clicks) The most possible clicked board is 
  0123
0 YRYR
1 RYRR
2 RYRY

```
