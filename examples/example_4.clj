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
(def s1 [{:format [:note :velocity]} sd-note-1 10])
(def s2 [{:format [:note :velocity]} sd-note-1 40])
(def s3 [{:format [:note :velocity]} 40 30])

; rest
(def r -1)


(def hh-riff-1 [{:format [:note]} r  h1 r  h1 r  h1 r  h1])
(def hh-riff-2 [{:format [:note]} r  h1 r  h1 r  h1 h2 h3])
(def hh-riff-3 [{:format [:note]} r  h1 r  h1 r  h1 h2 h3])

(def rf1 [{:format [:velocity]
           :spacing-inverted false 
           :spacing (/ 1.0 16) 
           :note sd-note-1}
          5 10 15 20 25 30 35 40])

(def sd-riff-1 [{:format [:note]} r  r  s2 r  r  r  s2 r ])
(def sd-riff-2 [{:format [:note]} r  r  s2 r  r  s1 s2 r ])
(def sd-riff-3 [{:format [:note]} r  r  s2 r  rf1])

(def bd-riff-1 [{:format [:note]} bd r  r  r  bd r  r  r ])

(def music
  (_ [{:spacing 8 :channel 1} hh-riff-1 hh-riff-2 hh-riff-1 hh-riff-3]
     [{:spacing 8 :channel 2} sd-riff-1 sd-riff-1 sd-riff-2 sd-riff-3]
     [{:spacing 8 :channel 3} bd-riff-1 bd-riff-1 bd-riff-1 bd-riff-1] _))


(create-midi-file
  music (str (char 0xA9) " 2009 Matthew Howlett") "/tmp/out4.mid")
