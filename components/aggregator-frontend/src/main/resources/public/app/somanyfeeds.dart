import 'package:angular2/angular2.dart';


@Component(selector: 'somanyfeeds')
@View(templateUrl: 'app/articles.html')
class SomanyfeedsComponent {
  List<String> sources = ['source1', 'source2'];
  List<String> articles = ['article 1', 'article 2'];
}
