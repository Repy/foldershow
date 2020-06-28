#!/bin/bash
"$(dirname $0)/image/bin/java" "-Xdock:icon=$(dirname $0)/../Resources/icon.png" -m "info.repy.foldershow/info.repy.foldershow.Main" "$1"
