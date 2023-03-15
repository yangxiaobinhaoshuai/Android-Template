import 'package:flutter/cupertino.dart';
import 'package:flutter_lib/abs_page.dart';

class ListPage extends AppBarPage {
  const ListPage({super.key, required super.title});

  @override
  Widget buildBody() {
    return ListView.builder(itemBuilder: buildItem,itemCount: 20,);
  }

  Widget buildItem(BuildContext context, int index) => Text(" index : $index");
}
