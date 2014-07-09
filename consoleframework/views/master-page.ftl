<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Bootstrap, from Twitter</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="${baseUrl}/static/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
    </style>
    <link href="${baseUrl}/static/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

    <#--<!-- HTML5 shim, for IE6-8 support of HTML5 elements &ndash;&gt;
    <!--[if lt IE 9]>
      <script src="../assets/js/html5shiv.js"></script>
    <![endif]&ndash;&gt;-->

    <#--<!-- Fav and touch icons &ndash;&gt;
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="../assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="../assets/ico/apple-touch-icon-114-precomposed.png">
      <link rel="apple-touch-icon-precomposed" sizes="72x72" href="../assets/ico/apple-touch-icon-72-precomposed.png">
                    <link rel="apple-touch-icon-precomposed" href="../assets/ico/apple-touch-icon-57-precomposed.png">
                                   <link rel="shortcut icon" href="../assets/ico/favicon.png">-->
  </head>

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="${baseUrl}/">Console Framework</a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active"><a href="${baseUrl}/">Home</a></li>
              <li><a href="${baseUrl}/downloads">Downloads</a></li>
              <li><a href="${baseUrl}/sources">Source code</a></li>
              <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Docs <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="#">Getting started</a></li>
                  <li><a href="#">Building from sources</a></li>
                  <li><a href="#">Examples</a></li>
                  <li class="divider"></li>
                  <li><a href="#">XAML reference</a></li>
                  <li><a href="#">Data binding reference</a></li>
                  <li><a href="#">Layout system reference</a></li>
                  <li><a href="#">Writing custom controls</a></li>
                </ul>
              </li>
            </ul>
            <#--<form class="navbar-form pull-right">
              <input class="span2" type="text" placeholder="Email">
              <input class="span2" type="password" placeholder="Password">
              <button type="submit" class="btn">Sign in</button>
            </form>-->
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container">

      <!-- Main hero unit for a primary marketing message or call to action -->
      <div class="hero-unit">
        <h1>Console Framework</h1>
        <p>Console framework is cross-platform toolkit that allows to develop TUI applications using C# and based on WPF-like concepts.</p>
        <p><a href="#" class="btn btn-primary btn-large">Learn more</a></p>
      </div>

      <!-- Example row of columns -->
      <div class="row">
        <div class="span4">
          <h2>Declarative markup</h2>
          <p>XAML support allows to design UI using the most productive way.</p>
          <p><a class="btn" href="#">View details</a></p>
        </div>
        <div class="span4">
          <h2>Data binding</h2>
          <p>No boilerplate code! Define your bindings in XAML markup directly.</p>
          <p><a class="btn" href="#">View details</a></p>
       </div>
        <div class="span4">
          <h2>Retained mode rendering system</h2>
          <p>Solid rendering system is integrated into events loop. Routed events system is similar to WPF too.</p>
          <p><a class="btn" href="#">View details</a></p>
        </div>
      </div>

        <div class="row">
        <div class="span4">
          <h2>WPF-compatible layout system</h2>
          <p>Simple and flexible layout system with panels and end-controls. You can use standard
          components or easily write custom one. <#--Each component is measured and arranged using WPF-compatible
          layout protocol, so you can use your experience in WPF to write your custom controls.--></p>
          <p><a class="btn" href="#">View details</a></p>
        </div>
        <div class="span4">
          <h2>A lot of controls available</h2>
          <p>For example: Grid, ScrollViewer, ListBox, ComboBox, TreeView</p>
          <p><a class="btn" href="#">View details</a></p>
       </div>
        <div class="span4">
          <h2>Crossplatform</h2>
          <p>Windows, Mac OS X and any Linux (32-bit or 64-bit) support</p>
          <p><a class="btn" href="#">View details</a></p>
        </div>
      </div>

      <hr>

      <footer>
        <p>&copy; elwood 2013</p>
      </footer>

    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${baseUrl}/static/index.js"></script>
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="${baseUrl}/static/bootstrap/js/bootstrap.js"></script>
    <#--<script src="../assets/js/bootstrap-transition.js"></script>
    <script src="../assets/js/bootstrap-alert.js"></script>
    <script src="../assets/js/bootstrap-modal.js"></script>
    <script src="../assets/js/bootstrap-dropdown.js"></script>
    <script src="../assets/js/bootstrap-scrollspy.js"></script>
    <script src="../assets/js/bootstrap-tab.js"></script>
    <script src="../assets/js/bootstrap-tooltip.js"></script>
    <script src="../assets/js/bootstrap-popover.js"></script>
    <script src="../assets/js/bootstrap-button.js"></script>
    <script src="../assets/js/bootstrap-collapse.js"></script>
    <script src="../assets/js/bootstrap-carousel.js"></script>
    <script src="../assets/js/bootstrap-typeahead.js"></script>-->

  </body>
</html>
