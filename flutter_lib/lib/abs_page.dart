import 'package:flutter/material.dart';

abstract class AppBarPage extends StatelessWidget {
  final String title;

  const AppBarPage({super.key, required this.title});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(title),
        ),
        body: buildBody());
  }

  Widget buildBody();
}
