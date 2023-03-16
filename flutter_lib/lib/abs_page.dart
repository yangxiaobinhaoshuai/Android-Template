import 'package:flutter/material.dart';

mixin AppBarWare {
  String get appbarTitle;

  String get logTag;

  Widget buildWhole(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(appbarTitle),
        ),
        body: buildBody());
  }

  Widget buildBody();

  void ld(String message) => debugPrint("$logTag => $message.");
}
