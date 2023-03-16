import 'package:flutter/material.dart';
import 'package:flutter_lib/abs_page.dart';

class ListPage extends StatefulWidget {
  final String title;

  const ListPage({super.key, required this.title});

  @override
  State<StatefulWidget> createState() => ListState();
}

class ListState extends State<ListPage> with AppBarWare {
  @override
  String get appbarTitle => widget.title;

  @override
  String get logTag => widget.runtimeType.toString();

  @override
  Widget build(BuildContext context) => buildWhole(context);

  @override
  Widget buildBody() {
    return ListView.builder(
      itemBuilder: buildItem,
      itemCount: 10,
    );
  }

  Widget buildItem(BuildContext context, int index) => Draggable(
        data: index,
        feedback: Container(
          color: Colors.pinkAccent,
          child: Container(
              alignment: Alignment.center,
              height: 40,
              width: 200,
              child: Text(
                "Drag index: $index",
                style: const TextStyle(fontSize: 16, color: Colors.white),
              )),
        ),
        child: _itemWrapper(index),
        dragAnchorStrategy: (Draggable<Object> draggable, BuildContext context,
                Offset position) =>
            const Offset(50, 50),
      );

  Widget _itemWrapper(int index) => DragTarget(
        builder: (BuildContext context, List<int?> candidateData,
            List<dynamic> rejectedData) {
          ld("builder, index: $index, candidateData: $candidateData, rejectedData: $rejectedData");
          return _actualItem(index);
        },
        onWillAccept: (int? data) {
          ld('onWillAccept, data: $data，cur index: $index');
          return true;
        },
        onLeave: (int? data) {
          ld("onLeave ,data: $data，cur index: $index");
        },
        onAccept: (int? data) {
          setState(() {});
          ld("onAccept, data: $data，cur index: $index");
        },
      );

  Widget _actualItem(int index) {
    return Container(
      margin:
          const EdgeInsetsDirectional.symmetric(vertical: 10, horizontal: 20),
      decoration: BoxDecoration(
        color: Colors.blueGrey,
        borderRadius: BorderRadius.circular(40),
      ),
      child: Column(
        children: [
          Center(
            child: Container(
              height: 40,
              padding: const EdgeInsets.only(top: 10),
              child: Text(
                " index : $index",
                style: const TextStyle(
                  fontSize: 14,
                  color: Colors.white,
                ),
              ),
            ),
          ),

          // list divider
          //const Divider(),
        ],
      ),
    );
  }
}
