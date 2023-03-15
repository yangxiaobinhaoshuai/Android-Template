import 'package:flutter/material.dart';
import 'package:flutter_lib/abs_page.dart';

class ListPage extends AppBarPage {
  const ListPage({super.key, required super.title});

  @override
  Widget buildBody() {
    return ListView.builder(
      itemBuilder: buildItem,
      itemCount: 20,
    );
  }

  Widget buildItem(BuildContext context, int index) => Draggable(
        feedback: Container(
          color: Colors.pinkAccent,
        ),
        child: Container(
          margin: const EdgeInsetsDirectional.symmetric(
              vertical: 10, horizontal: 20),
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
        ),
      );
}
