import 'package:flutter/material.dart';
import 'package:flutter_lib/abs_page.dart';

class ButtonPage extends AppBarPage {
  const ButtonPage({super.key, required super.title});

  @override
  Widget buildBody() => Center(
        child: Container(
          margin: const EdgeInsets.only(top: 60),
          child: Column(children: [

            DragTarget(builder: (BuildContext context,
                List<dynamic> candidateData, List<dynamic> rejectedData) {
              debugPrint(
                  "DragTarget, candidateData:$candidateData, rejectedData:$rejectedData");

                return Container(
                    height: 80,
                    width: 100,
                    color: Colors.green,
                    alignment: Alignment.center,
                    margin: const EdgeInsets.symmetric(vertical: 20),
                    child: const Text(
                      "DragTarget",
                      style: TextStyle(color: Colors.white),
                    ));
              },

              onWillAccept: (dynamic data) {
                debugPrint('onWillAccept:$data');
                return true;
              },
              onAccept: (dynamic data) {
                debugPrint('onAccept:$data');
              },
              onLeave: (dynamic data) {
                debugPrint('onLeave:$data');
              },
            ),

            Container(
              margin: const EdgeInsets.only(top: 120),
              child: Draggable(
                feedback: _feedback(),
                childWhenDragging: _childWhenDragging(),
                child: _button(),
              ),
            ),
          ]),
        ),
      );

  Widget _childWhenDragging() => Container(
        height: 100,
        width: 100,
        alignment: Alignment.center,
        decoration: BoxDecoration(
            color: Colors.cyan, borderRadius: BorderRadius.circular(10)),
        child: const Text(
          '灰色',
          style: TextStyle(color: Colors.white, fontSize: 18),
        ),
      );

  Widget _feedback() {
    return Container(
      height: 100,
      width: 100,
      alignment: Alignment.center,
      decoration: BoxDecoration(
          color: Colors.blue, borderRadius: BorderRadius.circular(10)),
      child: const Text(
        'Moving',
        style: TextStyle(color: Colors.white, fontSize: 18),
      ),
    );
  }

  Widget _button() => Container(
      height: 100,
      width: 100,
      alignment: Alignment.center,
      color: Colors.yellow,
      child: TextButton(onPressed: () => {}, child: const Text("I'm button!")));
}
