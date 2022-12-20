package pgdp.trains.processing.resources;

public class JSONDataset {
  public static final String iceBasicBlob = """
      {
        "train": {
          "name": "ICE 881",
          "line": "25",
          "admin": "80____",
          "number": "881",
          "type": "ICE",
          "operator": {
            "name": "DB Fernverkehr AG",
            "icoX": 1
          }
        },
        "finalDestination": "Hamburg-Altona",
        "jid": "1|201110|0|80|2112022",
        "currentStation": {
          "id": "8000261",
          "title": "München Hbf",
          "coordinates": {
            "lng": 11.558744,
            "lat": 48.140364
          },
          "products": [
            {
              "name": "ICE",
              "type": "ICE"
            }
          ]
        },
        "stops": [
          {
            "station": {
              "id": "8002553",
              "title": "Hamburg-Altona",
              "coordinates": {
                "lng": 9.93486,
                "lat": 53.552571
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T12:44:00.000Z",
              "time": "2022-11-02T12:44:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8002548",
              "title": "Hamburg Dammtor",
              "coordinates": {
                "lng": 9.989533,
                "lat": 53.560841
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T12:53:00.000Z",
              "time": "2022-11-02T12:53:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8002549",
              "title": "Hamburg Hbf",
              "coordinates": {
                "lng": 10.00636,
                "lat": 53.553533
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T13:01:00.000Z",
              "time": "2022-11-02T13:01:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000147",
              "title": "Hamburg-Harburg",
              "coordinates": {
                "lng": 9.991591,
                "lat": 53.456198
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T13:12:00.000Z",
              "time": "2022-11-02T13:12:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000238",
              "title": "Lüneburg",
              "coordinates": {
                "lng": 10.419873,
                "lat": 53.249742
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T13:29:00.000Z",
              "time": "2022-11-02T13:29:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000152",
              "title": "Hannover Hbf",
              "coordinates": {
                "lng": 9.741763,
                "lat": 52.377079
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T14:26:00.000Z",
              "time": "2022-11-02T14:26:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000128",
              "title": "Göttingen",
              "coordinates": {
                "lng": 9.926257,
                "lat": 51.536758
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T15:02:00.000Z",
              "time": "2022-11-02T15:02:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8003200",
              "title": "Kassel-Wilhelmshöhe",
              "coordinates": {
                "lng": 9.446845,
                "lat": 51.312998
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T15:23:00.000Z",
              "time": "2022-11-02T15:23:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000115",
              "title": "Fulda",
              "coordinates": {
                "lng": 9.684178,
                "lat": 50.554794
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T15:56:00.000Z",
              "time": "2022-11-02T15:56:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000260",
              "title": "Würzburg Hbf",
              "coordinates": {
                "lng": 9.93593,
                "lat": 49.802163
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T17:10:00.000Z",
              "time": "2022-11-02T17:10:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000284",
              "title": "Nürnberg Hbf",
              "coordinates": {
                "lng": 11.08227,
                "lat": 49.445435
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:07:00.000Z",
              "time": "2022-11-02T18:07:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000183",
              "title": "Ingolstadt Hbf",
              "coordinates": {
                "lng": 11.437335,
                "lat": 48.744538
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:41:00.000Z",
              "time": "2022-11-02T18:41:00.000Z",
              "reihung": true
            }
          },
          {
            "station": {
              "id": "8000261",
              "title": "München Hbf",
              "coordinates": {
                "lng": 11.558744,
                "lat": 48.140364
              },
              "products": [
                {
                  "name": "ICE",
                  "type": "ICE     "
                }
              ]
            },
            "arrival": {
              "scheduledPlatform": "19",
              "platform": "15",
              "scheduledTime": "2022-11-02T19:21:00.000Z",
              "time": "2022-11-02T19:36:00.000Z",
              "delay": 15,
              "reihung": true
            }
          }
        ],
        "arrival": {
          "scheduledPlatform": "19",
          "platform": "15",
          "scheduledTime": "2022-11-02T19:21:00.000Z",
          "time": "2022-11-02T19:36:00.000Z",
          "delay": 15
        }
      }
      """;

  public static final String str29CanceledBlob = """
      {
        "train": {
          "name": "STR 29",
          "line": "29",
          "admin": "swm002",
          "number": "25367",
          "type": "STR",
          "operator": {
            "name": "Nahreisezug",
            "icoX": 21
          }
        },
        "finalDestination": "Willibaldplatz, München",
        "jid": "1|1072892|4|80|2112022",
        "cancelled": true,
        "currentStation": {
          "id": "621504",
          "title": "Hauptbahnhof Süd, München",
          "coordinates": {
            "lng": 11.55913,
            "lat": 48.139258
          },
          "products": []
        },
        "stops": [
          {
            "station": {
              "id": "373606",
              "title": "Willibaldplatz, München",
              "coordinates": {
                "lng": 11.488664,
                "lat": 48.140462
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:16:00.000Z",
              "time": "2022-11-02T19:16:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "620013",
              "title": "Agnes-Bernauer-Platz, München",
              "coordinates": {
                "lng": 11.496556,
                "lat": 48.13977
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:17:00.000Z",
              "time": "2022-11-02T19:17:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "625368",
              "title": "Fürstenrieder Straße, München",
              "coordinates": {
                "lng": 11.503586,
                "lat": 48.139707
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:19:00.000Z",
              "time": "2022-11-02T19:19:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "625362",
              "title": "Agnes-Bernauer-Straße, München",
              "coordinates": {
                "lng": 11.510732,
                "lat": 48.139375
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:20:00.000Z",
              "time": "2022-11-02T19:20:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "625377",
              "title": "Lautensackstraße, München",
              "coordinates": {
                "lng": 11.51822,
                "lat": 48.139617
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:22:00.000Z",
              "time": "2022-11-02T19:22:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "639151",
              "title": "Am Lokschuppen, München",
              "coordinates": {
                "lng": 11.524917,
                "lat": 48.140642
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:24:00.000Z",
              "time": "2022-11-02T19:24:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "620284",
              "title": "Barthstraße, München",
              "coordinates": {
                "lng": 11.53023,
                "lat": 48.140292
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:25:00.000Z",
              "time": "2022-11-02T19:25:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "625386",
              "title": "Trappentreustraße, München",
              "coordinates": {
                "lng": 11.53521,
                "lat": 48.139986
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:27:00.000Z",
              "time": "2022-11-02T19:27:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "623082",
              "title": "Schrenkstraße, München",
              "coordinates": {
                "lng": 11.541286,
                "lat": 48.139608
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:28:00.000Z",
              "time": "2022-11-02T19:28:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "624274",
              "title": "Holzapfelstraße, München",
              "coordinates": {
                "lng": 11.547633,
                "lat": 48.139141
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:29:00.000Z",
              "time": "2022-11-02T19:29:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "790013",
              "title": "Hermann-Lingg-Straße, München",
              "coordinates": {
                "lng": 11.551381,
                "lat": 48.138979
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:31:00.000Z",
              "time": "2022-11-02T19:31:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "624949",
              "title": "Holzkirchner Bahnhof, München",
              "coordinates": {
                "lng": 11.555561,
                "lat": 48.139105
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:32:00.000Z",
              "time": "2022-11-02T19:32:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          },
          {
            "station": {
              "id": "621504",
              "title": "Hauptbahnhof Süd, München",
              "coordinates": {
                "lng": 11.55913,
                "lat": 48.139258
              },
              "products": [
                {
                  "name": "STR 29",
                  "line": "29",
                  "type": "STR     "
                }
              ]
            },
            "arrival": {
              "scheduledTime": "2022-11-02T19:34:00.000Z",
              "time": "2022-11-02T19:34:00.000Z",
              "cancelled": true
            },
            "cancelled": true
          }
        ],
        "messages": [
          {
            "type": "P",
            "code": "",
            "icoX": 24,
            "txtN": "Fahrt fällt aus"
          }
        ],
        "arrival": {
          "scheduledTime": "2022-11-02T19:34:00.000Z",
          "time": "2022-11-02T19:34:00.000Z",
          "cancelled": true
        }
      }
      """;

  public static final String re8BasicBlob = """
      {
        "train": {
          "name": "RE 8",
          "line": "8",
          "admin": "800734",
          "number": "57061",
          "type": "RE",
          "operator": {
            "name": "DB Regio AG Bayern",
            "icoX": 20
          }
        },
        "finalDestination": "Treuchtlingen",
        "jid": "1|260813|0|80|2112022",
        "currentStation": {
          "id": "8000261",
          "title": "München Hbf",
          "coordinates": {
            "lng": 11.558744,
            "lat": 48.140364
          },
          "products": [
            {
              "name": "RB",
              "type": "RB      "
            },
            {
              "name": "RE",
              "type": "DPN     "
            },
            {
              "name": "RE",
              "type": "RE      "
            }
          ]
        },
        "stops": [
          {
            "station": {
              "id": "8000122",
              "title": "Treuchtlingen",
              "coordinates": {
                "lng": 10.908104,
                "lat": 48.961142
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T17:35:00.000Z",
              "time": "2022-11-02T17:35:00.000Z"
            }
          },
          {
            "station": {
              "id": "8004731",
              "title": "Otting-Weilheim",
              "coordinates": {
                "lng": 10.81894,
                "lat": 48.880877
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T17:42:00.000Z",
              "time": "2022-11-02T17:42:00.000Z"
            }
          },
          {
            "station": {
              "id": "8000078",
              "title": "Donauwörth",
              "coordinates": {
                "lng": 10.771522,
                "lat": 48.714028
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T17:58:00.000Z",
              "time": "2022-11-02T17:58:00.000Z"
            }
          },
          {
            "station": {
              "id": "8003989",
              "title": "Mertingen Bahnhof",
              "coordinates": {
                "lng": 10.825143,
                "lat": 48.66047
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:03:00.000Z",
              "time": "2022-11-02T18:03:00.000Z"
            }
          },
          {
            "station": {
              "id": "8004451",
              "title": "Nordendorf",
              "coordinates": {
                "lng": 10.837305,
                "lat": 48.593438
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:09:00.000Z",
              "time": "2022-11-02T18:09:00.000Z"
            }
          },
          {
            "station": {
              "id": "8003952",
              "title": "Meitingen",
              "coordinates": {
                "lng": 10.848056,
                "lat": 48.545921
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:13:00.000Z",
              "time": "2022-11-02T18:13:00.000Z"
            }
          },
          {
            "station": {
              "id": "8000662",
              "title": "Augsburg-Oberhausen",
              "coordinates": {
                "lng": 10.872516,
                "lat": 48.380744
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:24:00.000Z",
              "time": "2022-11-02T18:24:00.000Z"
            }
          },
          {
            "station": {
              "id": "8000013",
              "title": "Augsburg Hbf",
              "coordinates": {
                "lng": 10.885595,
                "lat": 48.365247
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:39:00.000Z",
              "time": "2022-11-02T18:39:00.000Z"
            }
          },
          {
            "station": {
              "id": "8000658",
              "title": "Augsburg Haunstetterstraße",
              "coordinates": {
                "lng": 10.900895,
                "lat": 48.355287
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:42:00.000Z",
              "time": "2022-11-02T18:42:00.000Z"
            }
          },
          {
            "station": {
              "id": "8000661",
              "title": "Augsburg-Hochzoll",
              "coordinates": {
                "lng": 10.94532,
                "lat": 48.353039
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:45:00.000Z",
              "time": "2022-11-02T18:45:00.000Z"
            }
          },
          {
            "station": {
              "id": "8003299",
              "title": "Kissing",
              "coordinates": {
                "lng": 10.959756,
                "lat": 48.300246
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:50:00.000Z",
              "time": "2022-11-02T18:50:00.000Z"
            }
          },
          {
            "station": {
              "id": "8004008",
              "title": "Mering-St Afra",
              "coordinates": {
                "lng": 10.970453,
                "lat": 48.275022
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:53:00.000Z",
              "time": "2022-11-02T18:53:00.000Z"
            }
          },
          {
            "station": {
              "id": "8003982",
              "title": "Mering",
              "coordinates": {
                "lng": 10.987848,
                "lat": 48.262976
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T18:56:00.000Z",
              "time": "2022-11-02T18:56:00.000Z"
            }
          },
          {
            "station": {
              "id": "8004158",
              "title": "München-Pasing",
              "coordinates": {
                "lng": 11.461633,
                "lat": 48.150036
              }
            },
            "departure": {
              "scheduledTime": "2022-11-02T19:15:00.000Z",
              "time": "2022-11-02T19:15:00.000Z"
            }
          },
          {
            "station": {
              "id": "8000261",
              "title": "München Hbf",
              "coordinates": {
                "lng": 11.558744,
                "lat": 48.140364
              },
              "products": [
                {
                  "name": "RB",
                  "type": "RB      "
                },
                {
                  "name": "RE",
                  "type": "DPN     "
                },
                {
                  "name": "RE",
                  "type": "RE      "
                }
              ]
            },
            "arrival": {
              "platform": "14",
              "scheduledTime": "2022-11-02T19:22:00.000Z",
              "time": "2022-11-02T19:31:00.000Z",
              "delay": 9
            }
          }
        ],
        "arrival": {
          "platform": "14",
          "scheduledTime": "2022-11-02T19:22:00.000Z",
          "time": "2022-11-02T19:31:00.000Z",
          "delay": 9
        }
      }
      """;
}
