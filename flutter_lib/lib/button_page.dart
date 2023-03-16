import 'package:flutter/material.dart';
import 'package:flutter_lib/abs_page.dart';

class ButtonPage extends AppBarPage {
  const ButtonPage({super.key, required super.title});

  @override
  Widget buildBody() => Center(
      child: Container(
          color: Colors.yellow,
          child: TextButton(
              onPressed: () => {}, child: const Text("I'm button!"))));
}
