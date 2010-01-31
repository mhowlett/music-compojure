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

(ns music-compojure
  (:import java.io.File)
  (:import javax.sound.midi.MidiSystem)
  (:import javax.sound.midi.Sequence)
  (:use music-compojure.event-creation)
  (:use music-compojure.notes)
  (:use music-compojure.util))

(def _ list)

(def *ppq* 1152)

(def *default-settings* 
  { :velocity 120 
    :up-velocity 0
    :channel 0
    :spacing 4
    :note c4
    :duration 0.9
    :format [:note :spacing] })


(defn- parse-map [track map current-tick current-settings]
  (cond

    (contains? map :velocity)
      (recur track (dissoc map :velocity) current-tick 
        (assoc current-settings :velocity (:velocity map)))

    (contains? map :format)
      (recur track (dissoc map :format) current-tick 
        (assoc current-settings :format (:format map)))

    (contains? map :note)
      (recur track (dissoc map :note) current-tick 
        (assoc current-settings :note (:note map)))

    (contains? map :up-velocity)
      (recur track (dissoc map :up-velocity) current-tick 
        (assoc current-settings :up-velocity (:up-velocity map)))

    (contains? map :channel)
      (recur track (dissoc map :channel) current-tick
        (assoc current-settings :channel (:channel map)))

    (contains? map :duration)
      (recur track (dissoc map :duration) current-tick
        (assoc current-settings :duration (:duration map)))

    (contains? map :program)
      (do 
        (.add 
          track 
          (create-program-change-event 
          (:channel current-settings) 
          (:program map) 
          current-tick))
        (recur 
          track 
          (dissoc map :program) 
          current-tick 
          current-settings))

    (contains? map :tempo)
      (do
        (.add track (create-tempo-event (:tempo map) current-tick))
        (recur track (dissoc map :tempo) current-tick current-settings))

    (contains? map :sustain)
      (do
        (if (= (:sustain map) :up)
          (.add 
            track 
            (create-sustain-up-event 
              (:channel current-settings) 
              current-tick))
          (.add 
            track 
            (create-sustain-down-event
              (:channel current-settings)
              current-tick)))
        (recur 
          track 
          (dissoc map :sustain) 
          current-tick
          current-settings))

    true
      current-settings))


(def parse-vector)

(defn- parse-list [track list spacing velocity current-tick current-settings]
  (doall (map
    #(if (vector? %)
      (parse-vector track % current-tick '() current-settings)
      (if (= r %)
        nil
        (do
          (.add track 
            (create-note-on-event  
              (:channel current-settings) 
              %
              velocity 
              current-tick))
          (.add track
            (create-note-off-event
              (:channel current-settings)
              %
              (:up-velocity current-settings)
              (+ (* (* (/ 4 spacing) *ppq*) (:duration current-settings))
                current-tick))))))
    list)))

(defn calc-event-data [vec settings]
  (let 
    [vec-desc (:format settings)
     note-index (index-of-any #{:note} vec-desc)
     note (if (nil? note-index)
      (:note settings)
      (get vec note-index))
     spacing-index (index-of-any #{:spacing} vec-desc)
     spacing (if (nil? spacing-index)
      (:spacing settings)
      (get vec spacing-index))
     velocity-index (index-of-any #{:velocity} vec-desc)
     velocity (if (nil? velocity-index)
      (:velocity settings)
      (get vec velocity-index))
    ]
    {:note note
     :spacing spacing
     :velocity velocity}))

(defn- parse-vector [track vector current-tick current current-settings]

  ; if finished, finish... the only exit point.
  (if (empty? vector)
    current-tick

    (let [item (first vector)]

      ; if map, parse it.
      (if (map? item)
        (do
          (recur track (rest vector) current-tick '() 
            (parse-map track item current-tick current-settings)))
    
        ; if vector, parse.
        (if (vector? item)
          (let [new-current-tick 
                (parse-vector track item current-tick '() current-settings)]
            (recur track (rest vector) new-current-tick '() current-settings))
     
          ; calculate the new current vector and keep going if not ready.
          (let [ncur (concat current [item])
                nreq (count (:format current-settings))] 
            (if (not= (count ncur) nreq)
              (recur track (rest vector) current-tick ncur current-settings)

              ; the current vector is ready to be processed.
              (let [event-data
                     (calc-event-data (vec ncur) current-settings)
                    notes-list 
                      (if (list? (:note event-data))
                        (:note event-data)
                        (cons (:note event-data) '()))]
                (do
                  (parse-list 
                    track 
                    notes-list 
		    (:spacing event-data)
                    (:velocity event-data)
                    current-tick
                    current-settings)
                  (recur 
                    track 
                    (rest vector) 
                    (+ (* (/ 4 (:spacing event-data)) *ppq*) current-tick)
                    '() 
                    current-settings))))))))))


(defn parse-music [track music]
  (if (vector? music)
    (parse-vector track music 0 '() *default-settings*)
    (parse-list track music 0 0 0 *default-settings*)))


(defn create-midi-file 
  ([music copyright filename]
    (let [seq (new Sequence Sequence/PPQ *ppq*)
          track (.createTrack seq)] 
      (do
        (if (not (nil? copyright))
          (.add track (create-copyright-event copyright 0)))
        (parse-music track music)
        (. MidiSystem write seq 0 (new File filename)))))
  ([music filename]
    (create-midi-file music nil filename)))

