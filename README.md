# Closed-Loop-Liquid-cooling-system
Design and implementation of a closed-loop liquid cooling system for high performance CPU using Arduino Uno. Also, include an android application to control the device remotely. 



Project Summary

The capability of a computer to execute various complex functions simultaneously, causes its CPU and other microelectronic components to generate excess heat. 
If this heat is not dissipated from the computer, it will drastically drop the performance of the computers. Therefore, it is required to cool an overheated CPU, back 
to its optimal operating temperature by removing this extra heat, so that it retains the best quality performance of the computer.

The main goal of this project is to design and implement a closed-loop liquid cooling system to cool a CPU to a user specified temperature which was set via an Android application
or via the used of buttons connected to the system. Apart from cooling a dynamic load such as a CPU, this system also cools static loads such as cooling the fish tank water.


For convenience and simplicity, this project was executed in four modules namely, the mechanical hardware, the control system, the software and the communication links between 
the webserver Arduino, the controller Arduino, the database and the Android application. Since this system is built to cool both static and dynamic loads, the Android application
is designed in such a way that, it allows the user to select between an on/off temperature controller and a PID temperature controller depending on the user desired precision to 
cool the required load. Once the user selects the intended temperature controller, the user can then select the desired temperature to which the CPU needs to be cooled. Then 
the controller Arduino controls the amount of power to the thermoelectric cooling module, in order to bring down the temperature of the load. The Android application then 
continuously monitors and displays this temperature of the load. The webserver Arduino serves as the center of communication for the entire system, as it allows the communication
between the controller Arduino, the database and the Android application. This set up is capable of displaying the temperature fluctuations of the load, over a period of 24 hours,
48 hours or 72 hours through the Android application as preferred by the user. It is also comprised of an emergency shut-off valve system to stop circulating water through the 
pipes, after detecting a leak. In addition, there is also an alert notification system which notifies the user when a leak occurs in the system or when the temperature goes
5 °C above the user set temperature.


The project was successfully tested on a static load, while meeting all the performance metric specifications described in table I. Further testing is required on a CPU to
determine the temperature stability under a dynamic load, but all other performance metrics were achieved. With necessary modifications to the project, this system could be 
used for liquid cooling purposes in the medical, transportation and military fields and also to improve the performance of supercomputers at a larger scale.
