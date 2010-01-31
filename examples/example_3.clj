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

(ns example-3
  (:use music-compojure)
  (:use music-compojure.notes))

(def X 127)
(def x 80)
(def o 0)

(def bs 
  {:a [X o o o X o o o X o o o]
   :b [o o X o X o o o X o X o]
   :c [x o x o X X o o x o x x]})

(def music
  [ {:tempo 640}
    [{:program 1 :note a2 :format [:velocity]}
       (:a bs) (:b bs) (:c bs) (:c bs) (:c bs)]
    [{:program 1 :note c4 :format [:velocity]}
       (:a bs) (:b bs) (:c bs) (:c bs) (:c bs)]
  ])

(create-midi-file
  music (str (char 0xA9) " 2009 Matthew Howlett") "/tmp/out3.mid")
