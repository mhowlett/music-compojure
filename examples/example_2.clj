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

(ns example-2
  (:import java.util.Random)
  (:use music-compojure)
  (:use music-compojure.notes)
  (:use music-compojure.generators))

(def rnd (Random.))

(def scale [0 2 4 5 7 9 11])
(def durations [2 4 4 8 8 8])

(def notes (take 100 (repeatedly #(vector 
                                    (+ 60 (get scale (. rnd nextInt 7)))
                                    (get durations (. rnd nextInt 6))))))

(def music
  [{:format [:note :spacing]} (vec notes)])

(create-midi-file music "/tmp/out2.mid")
