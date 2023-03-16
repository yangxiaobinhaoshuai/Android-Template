import 'package:flutter/material.dart';
import 'package:flutter_lib/abs_page.dart';

class ReorderListPage extends StatefulWidget {
  final String title;

  const ReorderListPage({super.key, required this.title});

  @override
  State<StatefulWidget> createState() => ReorderListState();
}

class ReorderListState extends State<ReorderListPage> with AppBarWare {
  @override
  String get appbarTitle => widget.title;

  @override
  String get logTag => runtimeType.toString();

  @override
  Widget build(BuildContext context) => buildWhole(context);

  @override
  Widget buildBody() => ReorderableListView.builder(
      itemBuilder: (BuildContext context, int index) {
        return _item(index);
      },
      scrollDirection: Axis.vertical,
      itemCount: 10,
      onReorder: (int oldIndex, int newIndex) {
        ld("onReorder, oldIndex: $oldIndex, newIndex: $newIndex");
        // setState(() {
        //
        // });
      });

  Widget _item(int index) => Container(
      margin: const EdgeInsets.symmetric(horizontal: 40, vertical: 10),
      color: Colors.amber,
      alignment: Alignment.center,
      key: Key(index.toString()),
      height: 40,
      child: Text(" index: $index"));
}
