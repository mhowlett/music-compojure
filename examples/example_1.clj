;  This file is part of music-compojure.
;  Copyright (c) Matthew Howlett 2009
;
;  music-compojure is free software: you can redistribute it and/or modify
;  it under the terms of the GNU General Public License as published by
;  the Free Software Foundation, either version 3 of the License, or
;  (at your option) any later version.
;
;  music-compojure is distributed in the hope that it will be useful,
;  but WITHOUT ANY WARRANTY; without even the implied warranty of
;  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;  GNU General Public License for more details.
;
;  You should have received a copy of the GNU General Public License
;  along with music-compojure.  If not, see <http://www.gnu.org/licenses/>.

(ns example-1
  (:use music-compojure)
  (:use music-compojure.notes))

(def melody1-rh 
 [  g4 8, f+4 8,  g4 4,
    b4 4,  d4 4,
    d5 8,  c5 8,  b4 8,  c5 8,
    d5 8,  b4 8,
    g4 4,  a4 8,  b4 8,
    a4 8,  d4 8,  d5 4, 
    g4 8,  d4 8,  d5 4,
    a4 4,  d4 4,
     r 4
])

(def melody1-lh
  [  r 4,  b3 4,
    g3 4,  d3 4,
     r 4,  g3 4,
   f+3 4,  e3 4,
     r 4, f+4 4,
     r 4,  e4 4,
     r 4, f+4 4,
    a3 4,  d3 4])

(def melody2-rh
  [ e4 8, f+4 8,  e4 4,
    a3 4,   r 4,
    g4 8, f+4 8,  e4 4,
    a3 4,   r 4,
    g4 8, f+4 8,  g4 8,  a4 8,
   (_ b4 d5 _) 4,  a4 8,  g4 8,
    e4 4,  d4 4
     r 2])

(def melody2-lh
  [  r 4,  a3 4,
    d3 4,  a2 4,
     r 4,  a3 4,
    d3 4,  a2 4
     r 4, (_ g2 d3 g3 _) 4,
     r 4, (_ a2 e3 a3 _) 4,
     r 2,
    d3 4,
    d2 4
])

(def music
  [{:spacing-inverted true}
    (_ [ {:tempo 240 :channel 1 :program 1 :duration 0.2}
          melody1-rh melody1-rh melody2-rh melody2-rh]
       [ {:channel 2 :program 1 :duration 0.4}
          melody1-lh melody1-lh melody2-lh melody2-lh] _) 0 ]
)

(create-midi-file
  music (str (char 0xA9) " 1994 Matthew Howlett") "/tmp/out1.mid")
