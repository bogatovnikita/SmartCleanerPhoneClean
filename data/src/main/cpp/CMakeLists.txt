cmake_minimum_required(VERSION 3.4.1)

add_library(
        clean-lib
        SHARED
        clean-lib.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        clean-lib
        ${log-lib})
