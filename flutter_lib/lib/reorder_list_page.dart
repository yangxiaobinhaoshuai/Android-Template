import 'dart:ffi';

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

  List<int> dataList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

  @override
  Widget build(BuildContext context) => buildWhole(context);

  @override
  Widget buildBody() => ReorderableListView.builder(
      itemBuilder: (BuildContext context, int index) {
        return _item(index);
      },
      scrollDirection: Axis.vertical,
      itemCount: dataList.length,
      onReorder: (int oldIndex, int newIndex) {
        ld("onReorder, oldIndex: $oldIndex, newIndex: $newIndex");
        int removed = dataList.removeAt(oldIndex);
        dataList.insert(newIndex, removed);
        setState(() => Void);
      });

  Widget _item(int index) => Container(
      margin: const EdgeInsets.symmetric(horizontal: 40, vertical: 10),
      color: Colors.amber,
      alignment: Alignment.center,
      key: Key(index.toString()),
      height: 40,
      child: Text(" index: ${dataList[index]}"));
}
