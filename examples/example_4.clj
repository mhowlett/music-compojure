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

(ns example-4
  (:use music-compojure))

; base drum
(def bd [{:format [:note :velocity]} 36 127])

; hihats and cymbals
(def h1 [{:format [:note :velocity]} 42 20])
(def h2 [{:format [:note :velocity]} 42 100])
(def h3 [{:format [:note :velocity]} 44 80])
(def c1 [{:format [:note :velocity]} 46 90])

; snare drums
(def sd-note-1 38)
(def s1 [{:format [:note :velocity]} sd-note-1 40])
(def s2 [{:format [:note :velocity]} 40 30])

; rest
(def r -1)


(defn interleave-constant [vect stride const]
 )


(def hh-riff-1 [{:format [:note]}
    h1 h2 h1 h2])

(def sd-riff-1 [{:format [:note]}
    s1 s1 s1 s2])

(def bd-riff-1 [{:format [:note]}
    bd bd bd bd])

(def music
  (_ [{:spacing 0.125 :spacing-inverted false :channel 1} hh-riff-1 hh-riff-1 hh-riff-1 hh-riff-1]
     [{:spacing 8 :channel 2} sd-riff-1 sd-riff-1 sd-riff-1 sd-riff-1]
     [{:spacing 8 :channel 3} bd-riff-1 bd-riff-1 bd-riff-1 bd-riff-1] _))


(create-midi-file
  music (str (char 0xA9) " 2009 Matthew Howlett") "/tmp/out4.mid")
